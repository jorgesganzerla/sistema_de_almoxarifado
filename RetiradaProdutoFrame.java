import javax.swing.*;
import java.awt.*;

public class RetiradaProdutoFrame extends JFrame {
    private JTextField txtNomePessoa, txtSetor, txtCodigo, txtQuantidade;
    private JComboBox<Produto> cbProdutos;

    public RetiradaProdutoFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Retirar Produto");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Nome da Pessoa:"), gbc);
        gbc.gridx = 1;
        txtNomePessoa = new JTextField(20);
        add(txtNomePessoa, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Setor:"), gbc);
        gbc.gridx = 1;
        txtSetor = new JTextField(20);
        add(txtSetor, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Produto:"), gbc);
        gbc.gridx = 1;
        cbProdutos = new JComboBox<>();
        atualizarProdutos();
        add(cbProdutos, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        txtCodigo = new JTextField(20);
        txtCodigo.setEditable(false);
        add(txtCodigo, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Quantidade:"), gbc);
        gbc.gridx = 1;
        txtQuantidade = new JTextField(20);
        add(txtQuantidade, gbc);

        cbProdutos.addActionListener(e -> {
            Produto p = (Produto) cbProdutos.getSelectedItem();
            if (p != null) txtCodigo.setText(String.valueOf(p.getCodigo()));
        });

        JButton btnRetirar = new JButton("Retirar");
        btnRetirar.addActionListener(e -> retirarProduto());
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        add(btnRetirar, gbc);

        pack();
        setLocationRelativeTo(null);
    }

    private void atualizarProdutos() {
        cbProdutos.removeAllItems();
        for (Produto p : DatabaseConnection.listarProdutos()) {
            if (p.getQuantidade() > 0) {
                cbProdutos.addItem(p);
            }
        }
    }

    private void retirarProduto() {
        try {
            String nomePessoa = txtNomePessoa.getText().trim();
            String setor = txtSetor.getText().trim();
            int codigo = Integer.parseInt(txtCodigo.getText().trim());
            int quantidade = Integer.parseInt(txtQuantidade.getText().trim());

            if (nomePessoa.isEmpty() || setor.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!");
                return;
            }

            if (DatabaseConnection.retirarProduto(nomePessoa, setor, codigo, quantidade)) {
                JOptionPane.showMessageDialog(this, "Produto retirado com sucesso!");
                atualizarProdutos();
                limparCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Produto não encontrado ou quantidade insuficiente!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Código e quantidade devem ser números!");
        }
    }

    private void limparCampos() {
        txtNomePessoa.setText("");
        txtSetor.setText("");
        txtQuantidade.setText("");
        cbProdutos.setSelectedIndex(-1);
        txtCodigo.setText("");
    }
}