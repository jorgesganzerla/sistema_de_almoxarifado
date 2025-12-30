import java.time.LocalDateTime;

public class Retirada {
    private String nomePessoa;
    private String setor;
    private String nomeProduto;
    private int codigoProduto;
    private int quantidade;
    private LocalDateTime dataRetirada;

    public Retirada(String nomePessoa, String setor, String nomeProduto, int codigoProduto, int quantidade) {
        this.nomePessoa = nomePessoa;
        this.setor = setor;
        this.nomeProduto = nomeProduto;
        this.codigoProduto = codigoProduto;
        this.quantidade = quantidade;
        this.dataRetirada = LocalDateTime.now();
    }

    public String getNomePessoa() { return nomePessoa; }
    public String getSetor() { return setor; }
    public String getNomeProduto() { return nomeProduto; }
    public int getCodigoProduto() { return codigoProduto; }
    public int getQuantidade() { return quantidade; }
    public LocalDateTime getDataRetirada() { return dataRetirada; }
}