package almoxarifado.service;

import java.util.List;

/** Monta o texto exibido nas telas de relatório. */
public final class FormatadorRelatorio {

    private FormatadorRelatorio() {
    }

    public static String montar(String titulo, List<String> linhas, String mensagemVazio) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(titulo).append(" ===\n\n");
        if (linhas.isEmpty()) {
            sb.append(mensagemVazio);
        } else {
            for (String linha : linhas) {
                sb.append(linha).append("\n");
            }
        }
        return sb.toString();
    }
}
