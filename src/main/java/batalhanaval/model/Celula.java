package batalhanaval.model;

import java.util.Optional;

public final class Celula {
    private StatusCelula status;
    private Embarcacao embarcacao;

    public Celula() {
        status = StatusCelula.VAZIA;
        embarcacao = null;
    }

    public StatusCelula getStatus() {
        return status;
    }

    public boolean hasEmbarcacao() {
        return embarcacao != null;
    }

    public Optional<String> getNomeEmbarcacao() {
        if (embarcacao == null) {
            return Optional.empty();
        }
        return Optional.of(embarcacao.getNome());
    }

    void posicionarEmbarcacao(Embarcacao embarcacao) {
        if (embarcacao == null) {
            throw new IllegalArgumentException("embarcacao nao pode ser nula");
        }
        if (this.embarcacao != null || status != StatusCelula.VAZIA) {
            throw new IllegalStateException("celula nao esta vazia");
        }

        this.embarcacao = embarcacao;
        status = StatusCelula.EMBARCACAO;
    }

    public StatusTiro receberImpacto() {
        if (status == StatusCelula.AGUA_ATINGIDA || status == StatusCelula.EMBARCACAO_ATINGIDA) {
            return StatusTiro.REPETIDO;
        }

        if (status == StatusCelula.VAZIA) {
            status = StatusCelula.AGUA_ATINGIDA;
            return StatusTiro.AGUA;
        }

        status = StatusCelula.EMBARCACAO_ATINGIDA;
        embarcacao.aplicarDano();
        if (embarcacao.isAfundado()) {
            return StatusTiro.AFUNDOU;
        }
        return StatusTiro.ACERTO;
    }
}
