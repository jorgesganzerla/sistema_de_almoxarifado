package almoxarifado.db;

import almoxarifado.model.Produto;
import almoxarifado.service.RegrasEstoque;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Acesso ao banco MySQL: cadastro, retirada, listagem e consultas de relatório. */
public final class DatabaseConnection {

    private static final Logger LOG = Logger.getLogger(DatabaseConnection.class.getName());

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        String senha = exigirSenha(DbConfig.senha());
        return DriverManager.getConnection(DbConfig.url(), DbConfig.usuario(), senha);
    }

    /** Garante que a senha foi configurada, em vez de tentar conectar com credencial vazia. */
    static String exigirSenha(String senha) throws SQLException {
        if (senha == null) {
            throw new SQLException("Variável de ambiente DB_PASSWORD não definida. "
                    + "Consulte o README para configurar a conexão.");
        }
        return senha;
    }

    public static void cadastrarProduto(String nome, int quantidade, String especificacao, String empresa) {
        RegrasEstoque.validarCadastro(nome, quantidade, empresa);
        String sql = "INSERT INTO produtos (nome, quantidade, especificacao, empresa) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome.trim());
            stmt.setInt(2, quantidade);
            stmt.setString(3, especificacao);
            stmt.setString(4, empresa.trim());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao cadastrar produto", e);
            throw new IllegalStateException("Erro ao cadastrar produto: " + e.getMessage(), e);
        }
    }

    /**
     * Registra a retirada de forma transacional.
     *
     * @return {@code true} se a retirada foi registrada; {@code false} se o produto
     *         não existe ou o estoque é insuficiente
     * @throws IllegalArgumentException se os dados da retirada forem inválidos
     * @throws IllegalStateException    se ocorrer erro de banco de dados
     */
    public static boolean retirarProduto(String nomePessoa, String setor, int codigoProduto, int quantidade) {
        RegrasEstoque.validarRetirada(nomePessoa, setor, quantidade);
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try {
                boolean retirado = executarRetirada(conn, nomePessoa.trim(), setor.trim(), codigoProduto, quantidade);
                if (retirado) {
                    conn.commit();
                } else {
                    conn.rollback();
                }
                return retirado;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erro ao registrar retirada", e);
            throw new IllegalStateException("Erro ao registrar retirada: " + e.getMessage(), e);
        }
    }

    private static boolean executarRetirada(Connection conn, String nomePessoa, String setor,
                                            int codigoProduto, int quantidade) throws SQLException {
        String nomeProduto;
        int estoque;
        // FOR UPDATE bloqueia a linha e evita que duas retiradas simultâneas gerem estoque negativo.
        String selectSql = "SELECT nome, quantidade FROM produtos WHERE codigo = ? FOR UPDATE";
        try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
            selectStmt.setInt(1, codigoProduto);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }
                nomeProduto = rs.getString("nome");
                estoque = rs.getInt("quantidade");
            }
        }

        if (!RegrasEstoque.podeRetirar(estoque, quantidade)) {
            return false;
        }

        String updateSql = "UPDATE produtos SET quantidade = quantidade - ? WHERE codigo = ?";
        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            updateStmt.setInt(1, quantidade);
            updateStmt.setInt(2, codigoProduto);
            updateStmt.executeUpdate();
        }

        String insertSql = "INSERT INTO retiradas (nome_pessoa, setor, codigo_produto, nome_produto, quantidade) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
            insertStmt.setString(1, nomePessoa);
            insertStmt.setString(2, setor);
            insertStmt.setInt(3, codigoProduto);
            insertStmt.setString(4, nomeProduto);
            insertStmt.setInt(5, quantidade);
            insertStmt.executeUpdate();
        }
        return true;
    }

    public static List<Produto> listarProdutos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                produtos.add(new Produto(rs.getInt("codigo"), rs.getString("nome"),
                        rs.getInt("quantidade"), rs.getString("especificacao"), rs.getString("empresa")));
            }
        } catch (SQLException e) {
            throw erroDeConsulta("listar produtos", e);
        }
        return produtos;
    }

    public static List<String> relatorioSetores(String nomeSetor) {
        String sql = "SELECT setor, nome_produto, SUM(quantidade) AS total FROM retiradas "
                + "WHERE setor LIKE ? GROUP BY setor, nome_produto ORDER BY setor, nome_produto";
        return consultar("relatório por setor", sql, nomeSetor,
                rs -> "Setor: " + rs.getString("setor") + " | Produto: " + rs.getString("nome_produto")
                        + " | Quantidade: " + rs.getInt("total"));
    }

    public static List<String> buscarRetiradaPorPessoa(String nomePessoa) {
        String sql = "SELECT * FROM retiradas WHERE nome_pessoa LIKE ?";
        return consultar("busca por pessoa", sql, nomePessoa,
                rs -> "Pessoa: " + rs.getString("nome_pessoa") + " | Setor: " + rs.getString("setor")
                        + " | Produto: " + rs.getString("nome_produto") + " | Qtd: " + rs.getInt("quantidade")
                        + " | Data: " + rs.getTimestamp("data_retirada"));
    }

    public static List<String> buscarRetiradaPorProduto(String nomeProduto) {
        String sql = "SELECT * FROM retiradas WHERE nome_produto LIKE ?";
        return consultar("busca por produto", sql, nomeProduto,
                rs -> "Produto: " + rs.getString("nome_produto") + " | Pessoa: " + rs.getString("nome_pessoa")
                        + " | Setor: " + rs.getString("setor") + " | Qtd: " + rs.getInt("quantidade")
                        + " | Data: " + rs.getTimestamp("data_retirada"));
    }

    /** Converte uma linha do ResultSet em texto. */
    @FunctionalInterface
    private interface LinhaParaTexto {
        String converter(ResultSet rs) throws SQLException;
    }

    private static List<String> consultar(String descricao, String sql, String termo, LinhaParaTexto conversor) {
        List<String> resultado = new ArrayList<>();
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + termo + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resultado.add(conversor.converter(rs));
                }
            }
        } catch (SQLException e) {
            throw erroDeConsulta(descricao, e);
        }
        return resultado;
    }

    private static IllegalStateException erroDeConsulta(String descricao, SQLException e) {
        LOG.log(Level.SEVERE, "Erro ao consultar: " + descricao, e);
        return new IllegalStateException("Erro ao consultar (" + descricao + "): " + e.getMessage(), e);
    }
}
