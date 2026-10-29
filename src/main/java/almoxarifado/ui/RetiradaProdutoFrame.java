package almoxarifado.ui;

import almoxarifado.db.DatabaseConnection;
import almoxarifado.model.Produto;
import almoxarifado.service.RegrasEstoque;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/** Tela de retirada de produtos do estoque. */
public class RetiradaProdutoFrame extends JFrame {
    private JTextField txtNomePessoa;
    private JTextField txtSetor;
    private JTextField txtCodigo;
    private JTextField txtQuantidade;
    private JComboBox<Produto> cbProdutos;

    public RetiradaProdutoFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Retirar Produto");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Nome da Pessoa:"), gbc);
        gbc.gridx = 1;
        txtNomePessoa = new JTextField(20);
        add(txtNomePessoa, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Setor:"), gbc);
        gbc.gridx = 1;
        txtSetor = new JTextField(20);
        add(txtSetor, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Produto:"), gbc);
        gbc.gridx = 1;
        cbProdutos = new JComboBox<>();
        add(cbProdutos, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        txtCodigo = new JTextField(20);
        txtCodigo.setEditable(false);
        add(txtCodigo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Quantidade:"), gbc);
        gbc.gridx = 1;
        txtQuantidade = new JTextField(20);
        add(txtQuantidade, gbc);

        // O listener é registrado antes de carregar os produtos para que o código
        // do primeiro item seja preenchido automaticamente.
        cbProdutos.addActionListener(e -> {
            Produto p = (Produto) cbProdutos.getSelectedItem();
            txtCodigo.setText(p == null ? "" : String.valueOf(p.getCodigo()));
        });

        JButton btnRetirar = new JButton("Retirar");
        btnRetirar.addActionListener(e -> retirarProduto());
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(btnRetirar, gbc);

        atualizarProdutos();
        pack();
        setLocationRelativeTo(null);
    }

    private void atualizarProdutos() {
        try {
            cbProdutos.removeAllItems();
            for (Produto p : DatabaseConnection.listarProdutos()) {
                if (p.getQuantidade() > 0) {
                    cbProdutos.addItem(p);
                }
            }
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos:\n" + ex.getMessage());
        }
    }

    private void retirarProduto() {
        try {
            String nomePessoa = txtNomePessoa.getText().trim();
            String setor = txtSetor.getText().trim();
            int codigo = RegrasEstoque.parseInteiro(txtCodigo.getText(), "Código do produto");
            int quantidade = RegrasEstoque.parseInteiro(txtQuantidade.getText(), "Quantidade");

            if (DatabaseConnection.retirarProduto(nomePessoa, setor, codigo, quantidade)) {
                JOptionPane.showMessageDialog(this, "Produto retirado com sucesso!");
                atualizarProdutos();
                limparCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Produto não encontrado ou quantidade insuficiente!");
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar com banco de dados:\n" + ex.getMessage());
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
