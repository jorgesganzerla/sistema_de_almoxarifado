package almoxarifado.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** Utilitário manual para verificar a conexão com o banco (fora da cobertura automatizada). */
public final class TesteConexao {

    private TesteConexao() {
    }

    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM produtos")) {
            System.out.println("Conexão com banco estabelecida com sucesso!");
            if (rs.next()) {
                System.out.println("Produtos no banco: " + rs.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println("ERRO DE CONEXÃO: " + e.getMessage() + " (código " + e.getErrorCode() + ")");
        }
    }
}
