package almoxarifado.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import almoxarifado.model.Produto;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Teste de integração: exercita as regras de negócio contra um MySQL real
 * (serviço iniciado pelo pipeline ou banco local de teste).
 */
class DatabaseConnectionIT {

    @BeforeAll
    static void prepararBanco() throws Exception {
        SchemaHelper.recriarEsquema();
    }

    @BeforeEach
    void limparTabelas() throws Exception {
        SchemaHelper.limparDados();
    }

    private static Produto buscar(String nome) {
        return DatabaseConnection.listarProdutos().stream()
                .filter(p -> p.getNome().equals(nome))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Produto não encontrado: " + nome));
    }

    @Test
    void cadastraEListaProduto() {
        DatabaseConnection.cadastrarProduto("Papel A4", 100, "75g", "Chamex");

        Produto p = buscar("Papel A4");
        assertEquals(100, p.getQuantidade());
        assertEquals("75g", p.getEspecificacao());
        assertEquals("Chamex", p.getEmpresa());
        assertTrue(p.getCodigo() > 0, "código deve ser gerado pelo banco");
    }

    @Test
    void cadastroComQuantidadeNegativaEhRejeitadoENadaEhGravado() {
        assertThrows(IllegalArgumentException.class,
                () -> DatabaseConnection.cadastrarProduto("Caneta", -5, "", "Bic"));
        assertTrue(DatabaseConnection.listarProdutos().isEmpty());
    }

    @Test
    void retiradaValidaDebitaEstoqueERegistraHistorico() {
        DatabaseConnection.cadastrarProduto("Caneta", 10, "", "Bic");
        int codigo = buscar("Caneta").getCodigo();

        assertTrue(DatabaseConnection.retirarProduto("Maria", "TI", codigo, 3));

        assertEquals(7, buscar("Caneta").getQuantidade());
        List<String> historico = DatabaseConnection.buscarRetiradaPorPessoa("Maria");
        assertEquals(1, historico.size());
        assertTrue(historico.get(0).contains("Setor: TI"));
        assertTrue(historico.get(0).contains("Qtd: 3"));
    }

    @Test
    void retiradaDoEstoqueTotalEhPermitida() {
        DatabaseConnection.cadastrarProduto("Grampeador", 5, "", "Rapid");
        int codigo = buscar("Grampeador").getCodigo();

        assertTrue(DatabaseConnection.retirarProduto("João", "RH", codigo, 5));

        assertEquals(0, buscar("Grampeador").getQuantidade());
    }

    @Test
    void retiradaAcimaDoEstoqueEhRecusadaSemAlterarNada() {
        DatabaseConnection.cadastrarProduto("Borracha", 2, "", "Faber");
        int codigo = buscar("Borracha").getCodigo();

        assertFalse(DatabaseConnection.retirarProduto("Ana", "Financeiro", codigo, 3));

        assertEquals(2, buscar("Borracha").getQuantidade());
        assertTrue(DatabaseConnection.buscarRetiradaPorPessoa("Ana").isEmpty());
    }

    @Test
    void retiradaComQuantidadeNegativaEhRejeitadaEEstoqueNaoAumenta() {
        DatabaseConnection.cadastrarProduto("Régua", 10, "", "Faber");
        int codigo = buscar("Régua").getCodigo();

        assertThrows(IllegalArgumentException.class,
                () -> DatabaseConnection.retirarProduto("Maria", "TI", codigo, -5));

        assertEquals(10, buscar("Régua").getQuantidade());
    }

    @Test
    void retiradaDeProdutoInexistenteRetornaFalso() {
        assertFalse(DatabaseConnection.retirarProduto("Maria", "TI", 999_999, 1));
    }

    @Test
    void relatorioPorSetorSomaAsQuantidadesRetiradas() {
        DatabaseConnection.cadastrarProduto("Toner", 20, "", "HP");
        int codigo = buscar("Toner").getCodigo();
        DatabaseConnection.retirarProduto("Maria", "TI", codigo, 2);
        DatabaseConnection.retirarProduto("Pedro", "TI", codigo, 3);
        DatabaseConnection.retirarProduto("Ana", "RH", codigo, 1);

        List<String> ti = DatabaseConnection.relatorioSetores("TI");

        assertEquals(1, ti.size());
        assertTrue(ti.get(0).contains("Quantidade: 5"));
    }

    @Test
    void buscasPorPessoaEProdutoAceitamTermoParcialSemDiferenciarMaiusculas() {
        DatabaseConnection.cadastrarProduto("Caneta Azul", 10, "", "Bic");
        int codigo = buscar("Caneta Azul").getCodigo();
        DatabaseConnection.retirarProduto("Mariana Souza", "TI", codigo, 1);

        assertEquals(1, DatabaseConnection.buscarRetiradaPorPessoa("mari").size());
        assertEquals(1, DatabaseConnection.buscarRetiradaPorProduto("CANETA").size());
        assertTrue(DatabaseConnection.buscarRetiradaPorPessoa("inexistente").isEmpty());
    }
}
