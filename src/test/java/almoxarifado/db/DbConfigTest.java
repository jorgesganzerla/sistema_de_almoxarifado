package almoxarifado.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DbConfigTest {

    private String urlOriginal;
    private String senhaOriginal;

    @BeforeEach
    void guardarPropriedades() {
        urlOriginal = System.getProperty("DB_URL");
        senhaOriginal = System.getProperty("DB_PASSWORD");
    }

    @AfterEach
    void restaurarPropriedades() {
        restaurar("DB_URL", urlOriginal);
        restaurar("DB_PASSWORD", senhaOriginal);
    }

    private static void restaurar(String chave, String valor) {
        if (valor == null) {
            System.clearProperty(chave);
        } else {
            System.setProperty(chave, valor);
        }
    }

    @Test
    void propriedadeDeSistemaTemPrioridadeSobreOPadrao() {
        System.setProperty("DB_URL", "jdbc:mysql://exemplo:3306/outro");
        assertEquals("jdbc:mysql://exemplo:3306/outro", DbConfig.url());
    }

    @Test
    void urlVaziaVoltaAoPadrao() {
        System.setProperty("DB_URL", "   ");
        assertEquals(DbConfig.PADRAO_URL, DbConfig.url());
    }

    @Test
    void senhaAusenteImpedeAConexaoComMensagemClara() {
        SQLException ex = assertThrows(SQLException.class, () -> DatabaseConnection.exigirSenha(null));
        assertTrue(ex.getMessage().contains("DB_PASSWORD"));
    }

    @Test
    void senhaVaziaEhAceitaPoisPodeSerLegitimaEmBancoLocal() throws SQLException {
        assertEquals("", DatabaseConnection.exigirSenha(""));
    }
}
