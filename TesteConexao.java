import java.sql.*;

public class TesteConexao {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            System.out.println("Conexão com banco estabelecida com sucesso!");
            
            // Teste simples
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM produtos");
            if (rs.next()) {
                System.out.println("Produtos no banco: " + rs.getInt(1));
            }
            
            conn.close();
        } catch (SQLException e) {
            System.out.println("ERRO DE CONEXÃO:");
            System.out.println("Mensagem: " + e.getMessage());
            System.out.println("Código: " + e.getErrorCode());
            e.printStackTrace();
        }
    }
}