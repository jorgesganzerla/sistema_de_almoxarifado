package almoxarifado.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import org.junit.jupiter.api.Test;

class FormatadorRelatorioTest {

    @Test
    void semResultadosMostraAMensagemPadrao() {
        String texto = FormatadorRelatorio.montar("RETIRADAS POR PESSOA", List.of(), "Nenhuma retirada para: Zé");
        assertEquals("=== RETIRADAS POR PESSOA ===\n\nNenhuma retirada para: Zé", texto);
    }

    @Test
    void comResultadosListaUmaLinhaPorItem() {
        String texto = FormatadorRelatorio.montar("RELATÓRIO", List.of("linha 1", "linha 2"), "vazio");
        assertEquals("=== RELATÓRIO ===\n\nlinha 1\nlinha 2\n", texto);
        assertFalse(texto.contains("vazio"));
    }
}
