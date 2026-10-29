package almoxarifado.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ProdutoTest {

    @Test
    void guardaOsDadosInformados() {
        Produto p = new Produto(7, "Papel A4", 25, "75g/m2", "Chamex");
        assertEquals(7, p.getCodigo());
        assertEquals("Papel A4", p.getNome());
        assertEquals(25, p.getQuantidade());
        assertEquals("75g/m2", p.getEspecificacao());
        assertEquals("Chamex", p.getEmpresa());
    }

    @Test
    void toStringMostraCodigoNomeEQuantidade() {
        assertEquals("7 - Papel A4 (Qtd: 25)", new Produto(7, "Papel A4", 25, "", "Chamex").toString());
    }
}
