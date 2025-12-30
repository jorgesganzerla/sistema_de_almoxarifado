import javax.swing.*;
import java.awt.*;

public class CadastroProdutoFrame extends JFrame {
    private JTextField txtNome, txtQuantidade, txtEspecificacao, txtEmpresa;

    public CadastroProdutoFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Cadastrar Produto");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        txtNome = new JTextField(20);
        add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Quantidade:"), gbc);
        gbc.gridx = 1;
        txtQuantidade = new JTextField(20);
        add(txtQuantidade, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Especificação:"), gbc);
        gbc.gridx = 1;
        txtEspecificacao = new JTextField(20);
        add(txtEspecificacao, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Empresa:"), gbc);
        gbc.gridx = 1;
        txtEmpresa = new JTextField(20);
        add(txtEmpresa, gbc);

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(e -> cadastrarProduto());
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(btnCadastrar, gbc);

        pack();
        setLocationRelativeTo(null);
    }

    private void cadastrarProduto() {
        try {
            String nome = txtNome.getText().trim();
            int quantidade = Integer.parseInt(txtQuantidade.getText().trim());
            String especificacao = txtEspecificacao.getText().trim();
            String empresa = txtEmpresa.getText().trim();

            if (nome.isEmpty() || empresa.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome e Empresa são obrigatórios!");
                return;
            }

            DatabaseConnection.cadastrarProduto(nome, quantidade, especificacao, empresa);
            JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");
            limparCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade deve ser um número!");
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar com banco de dados:\n" + ex.getMessage());
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtQuantidade.setText("");
        txtEspecificacao.setText("");
        txtEmpresa.setText("");
    }
}