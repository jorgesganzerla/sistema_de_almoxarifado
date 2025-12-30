import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/almoxarifado";
    private static final String USER = "root";
    private static final String PASSWORD = "123456789"; // Coloque sua senha do MySQL aqui

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void cadastrarProduto(String nome, int quantidade, String especificacao, String empresa) {
        String sql = "INSERT INTO produtos (nome, quantidade, especificacao, empresa) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setInt(2, quantidade);
            stmt.setString(3, especificacao);
            stmt.setString(4, empresa);
            int rows = stmt.executeUpdate();
            System.out.println("Produto cadastrado. Linhas afetadas: " + rows);
        } catch (SQLException e) {
            System.out.println("ERRO ao cadastrar produto: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao cadastrar produto: " + e.getMessage());
        }
    }

    public static boolean retirarProduto(String nomePessoa, String setor, int codigoProduto, int quantidade) {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            
            String selectSql = "SELECT nome, quantidade FROM produtos WHERE codigo = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectSql);
            selectStmt.setInt(1, codigoProduto);
            ResultSet rs = selectStmt.executeQuery();
            
            if (rs.next() && rs.getInt("quantidade") >= quantidade) {
                String updateSql = "UPDATE produtos SET quantidade = quantidade - ? WHERE codigo = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setInt(1, quantidade);
                updateStmt.setInt(2, codigoProduto);
                updateStmt.executeUpdate();
                
                String insertSql = "INSERT INTO retiradas (nome_pessoa, setor, codigo_produto, nome_produto, quantidade) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setString(1, nomePessoa);
                insertStmt.setString(2, setor);
                insertStmt.setInt(3, codigoProduto);
                insertStmt.setString(4, rs.getString("nome"));
                insertStmt.setInt(5, quantidade);
                insertStmt.executeUpdate();
                
                conn.commit();
                return true;
            }
            conn.rollback();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Produto> listarProdutos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                produtos.add(new Produto(rs.getInt("codigo"), rs.getString("nome"), 
                    rs.getInt("quantidade"), rs.getString("especificacao"), rs.getString("empresa")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produtos;
    }

    public static List<String> relatorioSetores(String nomeSetor) {
        List<String> resultado = new ArrayList<>();
        String sql = "SELECT setor, nome_produto, SUM(quantidade) as total FROM retiradas WHERE setor LIKE ? GROUP BY setor, nome_produto ORDER BY setor, nome_produto";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nomeSetor + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                resultado.add("Setor: " + rs.getString("setor") + " | Produto: " + rs.getString("nome_produto") + " | Quantidade: " + rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public static List<String> buscarRetiradaPorPessoa(String nomePessoa) {
        List<String> resultado = new ArrayList<>();
        String sql = "SELECT * FROM retiradas WHERE nome_pessoa LIKE ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nomePessoa + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                resultado.add("Pessoa: " + rs.getString("nome_pessoa") + " | Setor: " + rs.getString("setor") + 
                    " | Produto: " + rs.getString("nome_produto") + " | Qtd: " + rs.getInt("quantidade") + 
                    " | Data: " + rs.getTimestamp("data_retirada"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public static List<String> buscarRetiradaPorProduto(String nomeProduto) {
        List<String> resultado = new ArrayList<>();
        String sql = "SELECT * FROM retiradas WHERE nome_produto LIKE ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nomeProduto + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                resultado.add("Produto: " + rs.getString("nome_produto") + " | Pessoa: " + rs.getString("nome_pessoa") + 
                    " | Setor: " + rs.getString("setor") + " | Qtd: " + rs.getInt("quantidade") + 
                    " | Data: " + rs.getTimestamp("data_retirada"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }
}