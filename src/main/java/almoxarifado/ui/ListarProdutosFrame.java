package almoxarifado.ui;

import almoxarifado.db.DatabaseConnection;
import almoxarifado.service.FormatadorListagem;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/** Tela de listagem de produtos com o estoque atual. */
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
        try {
            txtProdutos.setText(FormatadorListagem.formatar(DatabaseConnection.listarProdutos()));
        } catch (IllegalStateException ex) {
            txtProdutos.setText("Não foi possível carregar os produtos:\n" + ex.getMessage());
        }
    }
}
