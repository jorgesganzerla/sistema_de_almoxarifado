import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Sistema de Almoxarifado");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1, 10, 10));

        JButton btnCadastro = new JButton("Cadastrar Produto");
        JButton btnRetirada = new JButton("Retirar Produto");
        JButton btnListar = new JButton("Listar Produtos");
        JButton btnRelatorio = new JButton("Relatórios");
        JButton btnSair = new JButton("Sair");

        btnCadastro.addActionListener(e -> new CadastroProdutoFrame().setVisible(true));
        btnRetirada.addActionListener(e -> new RetiradaProdutoFrame().setVisible(true));
        btnListar.addActionListener(e -> new ListarProdutosFrame().setVisible(true));
        btnRelatorio.addActionListener(e -> new RelatorioFrame().setVisible(true));
        btnSair.addActionListener(e -> System.exit(0));

        add(btnCadastro);
        add(btnRetirada);
        add(btnListar);
        add(btnRelatorio);
        add(btnSair);

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}