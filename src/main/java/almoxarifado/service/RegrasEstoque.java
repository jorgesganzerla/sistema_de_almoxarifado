package almoxarifado.service;

/**
 * Regras de negócio do almoxarifado, isoladas da interface gráfica e do banco
 * para que possam ser verificadas por testes unitários rápidos.
 */
public final class RegrasEstoque {

    /** Tamanho máximo dos campos de texto (VARCHAR(100) no banco). */
    public static final int TAMANHO_MAXIMO_TEXTO = 100;

    private RegrasEstoque() {
    }

    /** Converte texto digitado em inteiro, com mensagem clara em caso de erro. */
    public static int parseInteiro(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " é obrigatório(a)!");
        }
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(campo + " deve ser um número inteiro!", e);
        }
    }

    /** Valida os dados de um novo produto. */
    public static void validarCadastro(String nome, int quantidade, String empresa) {
        if (estaVazio(nome) || estaVazio(empresa)) {
            throw new IllegalArgumentException("Nome e Empresa são obrigatórios!");
        }
        if (nome.trim().length() > TAMANHO_MAXIMO_TEXTO || empresa.trim().length() > TAMANHO_MAXIMO_TEXTO) {
            throw new IllegalArgumentException("Nome e Empresa devem ter no máximo "
                    + TAMANHO_MAXIMO_TEXTO + " caracteres!");
        }
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa!");
        }
    }

    /** Valida os dados de uma retirada. */
    public static void validarRetirada(String nomePessoa, String setor, int quantidade) {
        if (estaVazio(nomePessoa) || estaVazio(setor)) {
            throw new IllegalArgumentException("Nome da pessoa e Setor são obrigatórios!");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero!");
        }
    }

    /** Indica se o estoque atual comporta a retirada solicitada. */
    public static boolean podeRetirar(int estoqueAtual, int quantidadeSolicitada) {
        return quantidadeSolicitada > 0 && estoqueAtual >= quantidadeSolicitada;
    }

    private static boolean estaVazio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}
