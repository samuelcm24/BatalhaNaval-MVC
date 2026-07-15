package batalhanaval.model;

import java.util.List;
import java.util.Objects;

public class Partida {
    private static final int MAX_CARGA_PORTA_AVIOES = 2;

    private final String nomeJogador;
    private final Tabuleiro tabuleiroJogador;
    private final Tabuleiro tabuleiroComputador;
    private StatusPartida status;
    private Turno turno;
    private int dicasRestantes;
    private int cargaPortaAvioes;

    public Partida(String nomeJogador, Tabuleiro tabuleiroJogador, Tabuleiro tabuleiroComputador) {
        if (nomeJogador == null || nomeJogador.isBlank()) {
            throw new IllegalArgumentException("nome do jogador nao pode ser nulo ou vazio");
        }
        this.nomeJogador = nomeJogador;
        this.tabuleiroJogador = Objects.requireNonNull(tabuleiroJogador, "tabuleiroJogador nao pode ser nulo");
        this.tabuleiroComputador = Objects.requireNonNull(tabuleiroComputador, "tabuleiroComputador nao pode ser nulo");
        status = StatusPartida.CONFIGURACAO;
        turno = null;
        dicasRestantes = 3;
        cargaPortaAvioes = 0;
    }

    public void iniciar() {
        if (status != StatusPartida.CONFIGURACAO) {
            throw new IllegalStateException("partida ja iniciada");
        }
        if (!tabuleiroJogador.temFrota() || !tabuleiroComputador.temFrota()) {
            throw new IllegalStateException("ambos os tabuleiros devem possuir frota");
        }
        status = StatusPartida.EM_ANDAMENTO;
        turno = Turno.JOGADOR;
    }

    public List<ResultadoDisparo> processarTiroJogador(Coordenada alvo, TipoDisparo tipo) {
        validarTiro(Turno.JOGADOR);
        Objects.requireNonNull(alvo, "alvo nao pode ser nulo");
        Objects.requireNonNull(tipo, "tipo nao pode ser nulo");

        if (isCoordenadaJaAtingida(tabuleiroComputador, alvo)) {
            throw new IllegalStateException("alvo ja atingido");
        }
        if (!podeUsarDisparo(tipo)) {
            throw new IllegalStateException("disparo indisponivel");
        }

        List<ResultadoDisparo> resultados = tabuleiroComputador.disparar(alvo, tipo);
        atualizarCargaAposDisparo(tipo);
        if (tabuleiroComputador.isFrotaDestruida()) {
            status = StatusPartida.VITORIA_JOGADOR;
        } else {
            turno = Turno.COMPUTADOR;
        }
        return resultados;
    }

    public StatusTiro processarTiroComputador(Coordenada alvo) {
        validarTiro(Turno.COMPUTADOR);
        Objects.requireNonNull(alvo, "alvo nao pode ser nulo");

        if (isCoordenadaJaAtingida(tabuleiroJogador, alvo)) {
            throw new IllegalStateException("alvo ja atingido");
        }

        StatusTiro resultado = tabuleiroJogador.receberImpacto(alvo);
        if (tabuleiroJogador.isFrotaDestruida()) {
            status = StatusPartida.VITORIA_COMPUTADOR;
        } else {
            turno = Turno.JOGADOR;
        }
        return resultado;
    }

    public boolean usarDica(Coordenada coordenada) {
        if (status != StatusPartida.EM_ANDAMENTO) {
            throw new IllegalStateException("partida nao esta em andamento");
        }
        Objects.requireNonNull(coordenada, "coordenada nao pode ser nula");
        if (dicasRestantes <= 0) {
            throw new IllegalStateException("nao ha dicas restantes");
        }
        dicasRestantes--;
        return tabuleiroComputador.temEmbarcacaoNaLinhaOuColuna(coordenada);
    }

    public boolean podeUsarDisparo(TipoDisparo tipo) {
        Objects.requireNonNull(tipo, "tipo nao pode ser nulo");
        return switch (tipo) {
            case SIMPLES -> tabuleiroJogador.temEmbarcacaoAtiva(Submarino.class);
            case CASCATA -> tabuleiroJogador.temEmbarcacaoAtiva(NavioEscolta.class);
            case ESTRELA -> tabuleiroJogador.temEmbarcacaoAtiva(Caca.class);
            case PORTA_AVIOES -> tabuleiroJogador.temEmbarcacaoAtiva(PortaAvioes.class)
                    && cargaPortaAvioes == MAX_CARGA_PORTA_AVIOES;
        };
    }

    public boolean isFinalizada() {
        return status == StatusPartida.VITORIA_JOGADOR || status == StatusPartida.VITORIA_COMPUTADOR;
    }

    public String getNomeJogador() {
        return nomeJogador;
    }

    public Tabuleiro getTabuleiroJogador() {
        return tabuleiroJogador;
    }

    public Tabuleiro getTabuleiroComputador() {
        return tabuleiroComputador;
    }

    public StatusPartida getStatus() {
        return status;
    }

    public Turno getTurno() {
        return turno;
    }

    public int getDicasRestantes() {
        return dicasRestantes;
    }

    public int getCargaPortaAvioes() {
        return cargaPortaAvioes;
    }

    private void validarTiro(Turno turnoEsperado) {
        if (status != StatusPartida.EM_ANDAMENTO) {
            throw new IllegalStateException("partida nao esta em andamento");
        }
        if (turno != turnoEsperado) {
            throw new IllegalStateException("turno invalido");
        }
    }

    private boolean isCoordenadaJaAtingida(Tabuleiro tabuleiro, Coordenada coordenada) {
        StatusCelula statusCelula = tabuleiro.getStatus(coordenada);
        return statusCelula == StatusCelula.AGUA_ATINGIDA || statusCelula == StatusCelula.EMBARCACAO_ATINGIDA;
    }

    private void atualizarCargaAposDisparo(TipoDisparo tipo) {
        if (tipo == TipoDisparo.PORTA_AVIOES) {
            cargaPortaAvioes = 0;
        } else if (cargaPortaAvioes < MAX_CARGA_PORTA_AVIOES) {
            cargaPortaAvioes++;
        }
    }
}
