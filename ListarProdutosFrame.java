import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class ListarProdutosFrame extends JFrame {
    private JTextArea txtProdutos;

    public ListarProdutosFrame() {
        initComponents();
        carregarProdutos();
    }

    private void initComponents() {
        setTitle("Lista de Produtos");
        setLayout(new BorderLayout());

        txtProdutos = new JTextArea(20, 50);
        txtProdutos.setEditable(false);
        txtProdutos.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(txtProdutos);

        add(scrollPane, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private void carregarProdutos() {
        List<Produto> produtos = DatabaseConnection.listarProdutos();
        Collections.sort(produtos, Comparator.comparing(Produto::getNome));

        StringBuilder sb = new StringBuilder();
        sb.append("=== LISTA DE PRODUTOS ===\n\n");
        sb.append(String.format("%-30s %s\n", "PRODUTO", "ESTOQUE"));
        sb.append("-".repeat(50)).append("\n");

        for (Produto p : produtos) {
            sb.append(String.format("%-30s %d\n", p.getNome(), p.getQuantidade()));
        }

        txtProdutos.setText(sb.toString());
    }
}