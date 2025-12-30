public class Produto {
    private static int proximoCodigo = 1;
    private int codigo;
    private String nome;
    private int quantidade;
    private String especificacao;
    private String empresa;

    public Produto(String nome, int quantidade, String especificacao, String empresa) {
        this.codigo = proximoCodigo++;
        this.nome = nome;
        this.quantidade = quantidade;
        this.especificacao = especificacao;
        this.empresa = empresa;
    }

    public Produto(int codigo, String nome, int quantidade, String especificacao, String empresa) {
        this.codigo = codigo;
        this.nome = nome;
        this.quantidade = quantidade;
        this.especificacao = especificacao;
        this.empresa = empresa;
    }

    public int getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public String getEspecificacao() { return especificacao; }
    public String getEmpresa() { return empresa; }

    @Override
    public String toString() {
        return codigo + " - " + nome + " (Qtd: " + quantidade + ")";
    }
}