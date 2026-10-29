package almoxarifado.model;

/** Produto armazenado no almoxarifado. */
public class Produto {
    private final int codigo;
    private final String nome;
    private final int quantidade;
    private final String especificacao;
    private final String empresa;

    public Produto(int codigo, String nome, int quantidade, String especificacao, String empresa) {
        this.codigo = codigo;
        this.nome = nome;
        this.quantidade = quantidade;
        this.especificacao = especificacao;
        this.empresa = empresa;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getEspecificacao() {
        return especificacao;
    }

    public String getEmpresa() {
        return empresa;
    }

    @Override
    public String toString() {
        return codigo + " - " + nome + " (Qtd: " + quantidade + ")";
    }
}
