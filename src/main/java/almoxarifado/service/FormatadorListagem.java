package almoxarifado.service;

import almoxarifado.model.Produto;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Monta o texto da listagem de produtos (ordem alfabética, sem diferenciar maiúsculas). */
public final class FormatadorListagem {

    private static final int LARGURA_LINHA = 50;

    private FormatadorListagem() {
    }

    public static String formatar(List<Produto> produtos) {
        List<Produto> ordenados = new ArrayList<>(produtos);
        ordenados.sort(Comparator.comparing(Produto::getNome, String.CASE_INSENSITIVE_ORDER));

        StringBuilder sb = new StringBuilder();
        sb.append("=== LISTA DE PRODUTOS ===\n\n");
        sb.append(String.format("%-30s %s\n", "PRODUTO", "ESTOQUE"));
        sb.append("-".repeat(LARGURA_LINHA)).append("\n");
        for (Produto p : ordenados) {
            sb.append(String.format("%-30s %d\n", p.getNome(), p.getQuantidade()));
        }
        return sb.toString();
    }
}
