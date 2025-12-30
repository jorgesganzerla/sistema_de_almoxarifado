import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class RelatorioFrame extends JFrame {
    private JTextArea txtResultado;
    private JTextField txtPesquisa;
    private JComboBox<String> cbTipoRelatorio;

    public RelatorioFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Relatórios");
        setLayout(new BorderLayout());

        JPanel panelTop = new JPanel(new FlowLayout());
        
        cbTipoRelatorio = new JComboBox<>(new String[]{
            "Relatório por Setor", 
            "Buscar por Pessoa", 
            "Buscar por Produto"
        });
        
        txtPesquisa = new JTextField(15);
        JButton btnPesquisar = new JButton("Pesquisar");
        
        panelTop.add(new JLabel("Tipo:"));
        panelTop.add(cbTipoRelatorio);
        panelTop.add(new JLabel("Pesquisa:"));
        panelTop.add(txtPesquisa);
        panelTop.add(btnPesquisar);

        txtResultado = new JTextArea(20, 50);
        txtResultado.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtResultado);

        btnPesquisar.addActionListener(e -> executarPesquisa());

        add(panelTop, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private void executarPesquisa() {
        String pesquisa = txtPesquisa.getText().trim();
        String tipo = (String) cbTipoRelatorio.getSelectedItem();
        StringBuilder resultado = new StringBuilder();

        switch (tipo) {
            case "Relatório por Setor":
                List<String> relatorioSetores = DatabaseConnection.relatorioSetores(pesquisa);
                resultado.append("=== RELATÓRIO DE SETORES ===\n\n");
                if (relatorioSetores.isEmpty()) {
                    resultado.append("Nenhum setor encontrado com o termo: ").append(pesquisa);
                } else {
                    for (String linha : relatorioSetores) {
                        resultado.append(linha).append("\n");
                    }
                }
                break;

            case "Buscar por Pessoa":
                List<String> retiradaPessoa = DatabaseConnection.buscarRetiradaPorPessoa(pesquisa);
                resultado.append("=== RETIRADAS POR PESSOA ===\n\n");
                if (retiradaPessoa.isEmpty()) {
                    resultado.append("Nenhuma retirada encontrada para: ").append(pesquisa);
                } else {
                    for (String linha : retiradaPessoa) {
                        resultado.append(linha).append("\n");
                    }
                }
                break;

            case "Buscar por Produto":
                List<String> retiradaProduto = DatabaseConnection.buscarRetiradaPorProduto(pesquisa);
                resultado.append("=== RETIRADAS POR PRODUTO ===\n\n");
                if (retiradaProduto.isEmpty()) {
                    resultado.append("Nenhuma retirada encontrada para o produto: ").append(pesquisa);
                } else {
                    for (String linha : retiradaProduto) {
                        resultado.append(linha).append("\n");
                    }
                }
                break;
        }

        txtResultado.setText(resultado.toString());
    }
}