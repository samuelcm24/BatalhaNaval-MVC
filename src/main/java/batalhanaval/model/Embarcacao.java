package batalhanaval.model;

public abstract class Embarcacao {
    private final String nome;
    private final int tamanho;
    private int vida;

    protected Embarcacao(String nome, int tamanho) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("nome nao pode ser nulo ou vazio");
        }
        if (tamanho <= 0) {
            throw new IllegalArgumentException("tamanho deve ser positivo");
        }

        this.nome = nome;
        this.tamanho = tamanho;
        this.vida = tamanho;
    }

    public String getNome() {
        return nome;
    }

    public int getTamanho() {
        return tamanho;
    }

    public int getVida() {
        return vida;
    }

    public void aplicarDano() {
        if (vida > 0) {
            vida--;
        }
    }

    public boolean isAfundado() {
        return vida == 0;
    }
}
