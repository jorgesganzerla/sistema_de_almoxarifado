package almoxarifado.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Recria o esquema a partir do database.sql (fonte única da verdade) em um banco
 * de TESTE. Recusa-se a rodar se a URL não apontar para um banco com "test" no nome,
 * para nunca apagar dados reais.
 */
final class SchemaHelper {

    private SchemaHelper() {
    }

    static void recriarEsquema() throws IOException, SQLException {
        String url = DbConfig.url();
        if (!url.contains("_test")) {
            throw new IllegalStateException("Por segurança, os testes de integração só rodam em um banco cujo nome "
                    + "contenha '_test'. DB_URL atual: " + url);
        }
        String script = Files.readString(Path.of("database.sql"), StandardCharsets.UTF_8);
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS retiradas");
            stmt.execute("DROP TABLE IF EXISTS produtos");
            for (String comando : dividir(script)) {
                String maiusculo = comando.toUpperCase();
                if (maiusculo.startsWith("CREATE DATABASE") || maiusculo.startsWith("USE ")) {
                    continue;
                }
                stmt.execute(comando);
            }
        }
    }

    static void limparDados() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM retiradas");
            stmt.execute("DELETE FROM produtos");
        }
    }

    private static java.util.List<String> dividir(String script) {
        StringBuilder semComentarios = new StringBuilder();
        for (String linha : script.split("\\R")) {
            if (!linha.trim().startsWith("--")) {
                semComentarios.append(linha).append("\n");
            }
        }
        java.util.List<String> comandos = new java.util.ArrayList<>();
        for (String parte : semComentarios.toString().split(";")) {
            if (!parte.trim().isEmpty()) {
                comandos.add(parte.trim());
            }
        }
        return comandos;
    }
}
