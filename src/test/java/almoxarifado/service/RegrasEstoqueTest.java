package almoxarifado.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RegrasEstoqueTest {

    @Nested
    @DisplayName("parseInteiro")
    class ParseInteiro {

        @Test
        void converteNumeroValido() {
            assertEquals(10, RegrasEstoque.parseInteiro("10", "Quantidade"));
        }

        @Test
        void ignoraEspacosNasBordas() {
            assertEquals(7, RegrasEstoque.parseInteiro("  7 ", "Quantidade"));
        }

        @Test
        void aceitaNumeroNegativoPoisAValidacaoDeFaixaEOutraRegra() {
            assertEquals(-3, RegrasEstoque.parseInteiro("-3", "Quantidade"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"abc", "1.5", "10x", "1,5"})
        void rejeitaTextoNaoNumerico(String entrada) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> RegrasEstoque.parseInteiro(entrada, "Quantidade"));
            assertEquals("Quantidade deve ser um número inteiro!", ex.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "   "})
        void rejeitaTextoVazio(String entrada) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> RegrasEstoque.parseInteiro(entrada, "Código"));
            assertTrue(ex.getMessage().contains("Código"));
        }

        @Test
        void rejeitaNulo() {
            assertThrows(IllegalArgumentException.class, () -> RegrasEstoque.parseInteiro(null, "Quantidade"));
        }
    }

    @Nested
    @DisplayName("validarCadastro")
    class ValidarCadastro {

        @Test
        void aceitaDadosValidos() {
            assertDoesNotThrow(() -> RegrasEstoque.validarCadastro("Papel A4", 100, "Kalunga"));
        }

        @Test
        void aceitaQuantidadeZero() {
            assertDoesNotThrow(() -> RegrasEstoque.validarCadastro("Caneta", 0, "Bic"));
        }

        @Test
        void rejeitaQuantidadeNegativa() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> RegrasEstoque.validarCadastro("Caneta", -1, "Bic"));
            assertEquals("Quantidade não pode ser negativa!", ex.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "   "})
        void rejeitaNomeVazio(String nome) {
            assertThrows(IllegalArgumentException.class, () -> RegrasEstoque.validarCadastro(nome, 1, "Bic"));
        }

        @Test
        void rejeitaNomeNulo() {
            assertThrows(IllegalArgumentException.class, () -> RegrasEstoque.validarCadastro(null, 1, "Bic"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "  "})
        void rejeitaEmpresaVazia(String empresa) {
            assertThrows(IllegalArgumentException.class, () -> RegrasEstoque.validarCadastro("Caneta", 1, empresa));
        }

        @Test
        void aceitaNomeComExatamenteOLimiteDeCaracteres() {
            String nome = "a".repeat(RegrasEstoque.TAMANHO_MAXIMO_TEXTO);
            assertDoesNotThrow(() -> RegrasEstoque.validarCadastro(nome, 1, "Bic"));
        }

        @Test
        void rejeitaNomeAcimaDoLimiteDeCaracteres() {
            String nome = "a".repeat(RegrasEstoque.TAMANHO_MAXIMO_TEXTO + 1);
            assertThrows(IllegalArgumentException.class, () -> RegrasEstoque.validarCadastro(nome, 1, "Bic"));
        }

        @Test
        void rejeitaEmpresaAcimaDoLimiteDeCaracteres() {
            String empresa = "e".repeat(RegrasEstoque.TAMANHO_MAXIMO_TEXTO + 1);
            assertThrows(IllegalArgumentException.class, () -> RegrasEstoque.validarCadastro("Caneta", 1, empresa));
        }
    }

    @Nested
    @DisplayName("validarRetirada")
    class ValidarRetirada {

        @Test
        void aceitaDadosValidos() {
            assertDoesNotThrow(() -> RegrasEstoque.validarRetirada("Maria", "TI", 5));
        }

        @Test
        void rejeitaPessoaVazia() {
            assertThrows(IllegalArgumentException.class, () -> RegrasEstoque.validarRetirada("", "TI", 5));
        }

        @Test
        void rejeitaSetorVazio() {
            assertThrows(IllegalArgumentException.class, () -> RegrasEstoque.validarRetirada("Maria", "  ", 5));
        }

        @Test
        void rejeitaQuantidadeZero() {
            assertThrows(IllegalArgumentException.class, () -> RegrasEstoque.validarRetirada("Maria", "TI", 0));
        }

        @Test
        void rejeitaQuantidadeNegativa() {
            // Sem esta regra, uma "retirada" negativa AUMENTARIA o estoque.
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> RegrasEstoque.validarRetirada("Maria", "TI", -5));
            assertEquals("Quantidade deve ser maior que zero!", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("podeRetirar")
    class PodeRetirar {

        @Test
        void permiteQuandoHaEstoqueSuficiente() {
            assertTrue(RegrasEstoque.podeRetirar(10, 3));
        }

        @Test
        void permiteRetirarExatamenteOEstoqueTotal() {
            assertTrue(RegrasEstoque.podeRetirar(5, 5));
        }

        @Test
        void recusaQuandoEstoqueInsuficiente() {
            assertFalse(RegrasEstoque.podeRetirar(5, 6));
        }

        @Test
        void recusaQuandoEstoqueZerado() {
            assertFalse(RegrasEstoque.podeRetirar(0, 1));
        }

        @Test
        void recusaQuantidadeZero() {
            assertFalse(RegrasEstoque.podeRetirar(10, 0));
        }

        @Test
        void recusaQuantidadeNegativa() {
            assertFalse(RegrasEstoque.podeRetirar(10, -4));
        }
    }
}
