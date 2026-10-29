package almoxarifado.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import almoxarifado.model.Produto;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class FormatadorListagemTest {

    private static Produto produto(int codigo, String nome, int quantidade) {
        return new Produto(codigo, nome, quantidade, "", "Fornecedor");
    }

    @Test
    void listaVaziaGeraApenasCabecalho() {
        String texto = FormatadorListagem.formatar(List.of());
        assertTrue(texto.contains("=== LISTA DE PRODUTOS ==="));
        assertTrue(texto.contains("PRODUTO"));
        assertFalse(texto.contains("Papel"));
    }

    @Test
    void ordenaAlfabeticamenteSemDiferenciarMaiusculas() {
        List<Produto> produtos = List.of(produto(1, "caneta", 1), produto(2, "Borracha", 2), produto(3, "apontador", 3));

        String texto = FormatadorListagem.formatar(produtos);

        int apontador = texto.indexOf("apontador");
        int borracha = texto.indexOf("Borracha");
        int caneta = texto.indexOf("caneta");
        assertTrue(apontador < borracha && borracha < caneta, "ordem esperada: apontador, Borracha, caneta");
    }

    @Test
    void mostraOEstoqueDeCadaProduto() {
        String texto = FormatadorListagem.formatar(List.of(produto(1, "Papel A4", 42)));
        assertTrue(texto.contains("Papel A4"));
        assertTrue(texto.contains("42"));
    }

    @Test
    void naoAlteraAListaRecebida() {
        List<Produto> original = new ArrayList<>(List.of(produto(1, "b", 1), produto(2, "a", 1)));
        FormatadorListagem.formatar(original);
        assertEquals("b", original.get(0).getNome());
    }
}
