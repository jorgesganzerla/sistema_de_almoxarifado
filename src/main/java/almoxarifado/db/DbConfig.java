package almoxarifado.db;

/**
 * Configuração da conexão lida de propriedades de sistema ou variáveis de ambiente.
 * Nenhuma credencial fica no código-fonte (requisito de segurança do projeto).
 */
public final class DbConfig {

    public static final String PADRAO_URL = "jdbc:mysql://localhost:3306/almoxarifado";
    public static final String PADRAO_USUARIO = "root";

    private DbConfig() {
    }

    public static String url() {
        return valorOuPadrao("DB_URL", PADRAO_URL);
    }

    public static String usuario() {
        return valorOuPadrao("DB_USER", PADRAO_USUARIO);
    }

    /** Retorna a senha configurada ou {@code null} se a variável DB_PASSWORD não existir. */
    public static String senha() {
        return buscar("DB_PASSWORD");
    }

    private static String valorOuPadrao(String chave, String padrao) {
        String valor = buscar(chave);
        return valor == null || valor.isBlank() ? padrao : valor;
    }

    private static String buscar(String chave) {
        String valor = System.getProperty(chave);
        return valor != null ? valor : System.getenv(chave);
    }
}
