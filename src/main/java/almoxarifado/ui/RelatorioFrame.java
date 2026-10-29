package almoxarifado.ui;

import almoxarifado.db.DatabaseConnection;
import almoxarifado.service.FormatadorRelatorio;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/** Tela de relatórios e consultas de retiradas. */
public class RelatorioFrame extends JFrame {
    private static final String POR_SETOR = "Relatório por Setor";
    private static final String POR_PESSOA = "Buscar por Pessoa";
    private static final String POR_PRODUTO = "Buscar por Produto";

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

        cbTipoRelatorio = new JComboBox<>(new String[] {POR_SETOR, POR_PESSOA, POR_PRODUTO});

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
        try {
            txtResultado.setText(gerarRelatorio(tipo, pesquisa));
        } catch (IllegalStateException ex) {
            txtResultado.setText("Não foi possível consultar o banco:\n" + ex.getMessage());
        }
    }

    private String gerarRelatorio(String tipo, String pesquisa) {
        switch (tipo) {
            case POR_SETOR:
                return FormatadorRelatorio.montar("RELATÓRIO DE SETORES",
                        DatabaseConnection.relatorioSetores(pesquisa),
                        "Nenhum setor encontrado com o termo: " + pesquisa);
            case POR_PESSOA:
                return FormatadorRelatorio.montar("RETIRADAS POR PESSOA",
                        DatabaseConnection.buscarRetiradaPorPessoa(pesquisa),
                        "Nenhuma retirada encontrada para: " + pesquisa);
            case POR_PRODUTO:
                return FormatadorRelatorio.montar("RETIRADAS POR PRODUTO",
                        DatabaseConnection.buscarRetiradaPorProduto(pesquisa),
                        "Nenhuma retirada encontrada para o produto: " + pesquisa);
            default:
                return "Tipo de relatório desconhecido.";
        }
    }
}
