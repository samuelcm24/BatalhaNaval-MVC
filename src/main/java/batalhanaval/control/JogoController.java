package batalhanaval.control;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import batalhanaval.model.Caca;
import batalhanaval.model.Coordenada;
import batalhanaval.model.Embarcacao;
import batalhanaval.model.IAAdversaria;
import batalhanaval.model.JogadorInfo;
import batalhanaval.model.NavioEscolta;
import batalhanaval.model.Orientacao;
import batalhanaval.model.Partida;
import batalhanaval.model.PortaAvioes;
import batalhanaval.model.ResultadoDisparo;
import batalhanaval.model.StatusCelula;
import batalhanaval.model.StatusPartida;
import batalhanaval.model.StatusTiro;
import batalhanaval.model.Submarino;
import batalhanaval.model.Tabuleiro;
import batalhanaval.model.TipoDisparo;

public class JogoController {
    private Partida partidaAtual;
    private final IAAdversaria bot;
    private final RankingController rankingController;
    private final Random random;
    private String nomePartidaAtual;
    private boolean modoRandomAtual;
    private boolean vitoriaRegistrada;

    public JogoController() {
        this(new IAAdversaria(), new RankingController(), new Random());
    }

    public JogoController(IAAdversaria bot, RankingController rankingController, Random random) {
        this.bot = Objects.requireNonNull(bot, "bot nao pode ser nulo");
        this.rankingController = Objects.requireNonNull(rankingController, "rankingController nao pode ser nulo");
        this.random = Objects.requireNonNull(random, "random nao pode ser nulo");
    }

    public void iniciarNovaPartida(String nome, boolean modoRandom) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("nome nao pode ser nulo ou vazio");
        }

        nomePartidaAtual = nome;
        modoRandomAtual = modoRandom;
        vitoriaRegistrada = false;

        Tabuleiro tabuleiroJogador = new Tabuleiro();
        Tabuleiro tabuleiroComputador = new Tabuleiro();
        tabuleiroComputador.posicionarFrotaPadraoAleatoria(random);
        if (modoRandom) {
            tabuleiroJogador.posicionarFrotaPadraoAleatoria(random);
        }

        partidaAtual = new Partida(nome, tabuleiroJogador, tabuleiroComputador);
        if (modoRandom) {
            partidaAtual.iniciar();
        }
    }

    public boolean podePosicionarEmbarcacaoJogador(
            Embarcacao embarcacao,
            int linha,
            int coluna,
            Orientacao orientacao) {
        validarPartidaEmConfiguracao();
        Objects.requireNonNull(embarcacao, "embarcacao nao pode ser nula");
        if (temEmbarcacaoDaClasse(embarcacao.getClass())) {
            return false;
        }
        return partidaAtual.getTabuleiroJogador().podePosicionar(
                embarcacao,
                new Coordenada(linha, coluna),
                orientacao);
    }

    public void posicionarEmbarcacaoJogador(Embarcacao embarcacao, int linha, int coluna, Orientacao orientacao) {
        validarPartidaEmConfiguracao();
        Objects.requireNonNull(embarcacao, "embarcacao nao pode ser nula");
        if (temEmbarcacaoDaClasse(embarcacao.getClass())) {
            throw new IllegalStateException("ja existe embarcacao desse tipo");
        }
        partidaAtual.getTabuleiroJogador().posicionarEmbarcacao(embarcacao, new Coordenada(linha, coluna), orientacao);
    }

    public boolean isConfiguracaoCompleta() {
        validarPartidaExistente();
        Tabuleiro tabuleiro = partidaAtual.getTabuleiroJogador();
        return tabuleiro.getEmbarcacoes().size() == 4
                && possuiExatamenteUm(tabuleiro, PortaAvioes.class)
                && possuiExatamenteUm(tabuleiro, NavioEscolta.class)
                && possuiExatamenteUm(tabuleiro, Submarino.class)
                && possuiExatamenteUm(tabuleiro, Caca.class);
    }

    public void iniciarPartidaConfigurada() {
        validarPartidaEmConfiguracao();
        if (!isConfiguracaoCompleta()) {
            throw new IllegalStateException("configuracao incompleta");
        }
        partidaAtual.iniciar();
    }

    public List<ResultadoDisparo> processarCliqueTiroJogador(int linha, int coluna, TipoDisparo tipo) {
        validarPartidaExistente();
        return partidaAtual.processarTiroJogador(new Coordenada(linha, coluna), tipo);
    }

    public Optional<ResultadoDisparo> executarTurnoComputador() {
        validarPartidaExistente();
        if (partidaAtual.isFinalizada()) {
            return Optional.empty();
        }
        Coordenada alvo = bot.calcularProximoAlvo(partidaAtual.getTabuleiroJogador());
        StatusTiro status = partidaAtual.processarTiroComputador(alvo);
        return Optional.of(new ResultadoDisparo(alvo, status));
    }

    public boolean usarDica(int linha, int coluna) {
        validarPartidaExistente();
        return partidaAtual.usarDica(new Coordenada(linha, coluna));
    }

    public boolean podeUsarDisparo(TipoDisparo tipo) {
        validarPartidaExistente();
        return partidaAtual.podeUsarDisparo(tipo);
    }

    public StatusCelula getStatusCelulaJogador(int linha, int coluna) {
        validarPartidaExistente();
        return partidaAtual.getTabuleiroJogador().getStatus(new Coordenada(linha, coluna));
    }

    public StatusCelula getStatusCelulaComputador(int linha, int coluna) {
        validarPartidaExistente();
        return partidaAtual.getTabuleiroComputador().getStatus(new Coordenada(linha, coluna));
    }

    public boolean hasEmbarcacaoJogador(int linha, int coluna) {
        validarPartidaExistente();
        return partidaAtual.getTabuleiroJogador().hasEmbarcacao(new Coordenada(linha, coluna));
    }

    public boolean hasEmbarcacaoComputador(int linha, int coluna) {
        validarPartidaExistente();
        return partidaAtual.getTabuleiroComputador().hasEmbarcacao(new Coordenada(linha, coluna));
    }

    public Optional<String> getNomeEmbarcacaoJogador(int linha, int coluna) {
        validarPartidaExistente();
        return partidaAtual.getTabuleiroJogador().getNomeEmbarcacao(new Coordenada(linha, coluna));
    }

    public String getNomeJogador() {
        validarPartidaExistente();
        return partidaAtual.getNomeJogador();
    }

    public StatusPartida getStatusPartida() {
        validarPartidaExistente();
        return partidaAtual.getStatus();
    }

    public int getDicasRestantes() {
        validarPartidaExistente();
        return partidaAtual.getDicasRestantes();
    }

    public int getCargaPortaAvioes() {
        validarPartidaExistente();
        return partidaAtual.getCargaPortaAvioes();
    }

    public List<Embarcacao> getEmbarcacoesJogador() {
        validarPartidaExistente();
        return partidaAtual.getTabuleiroJogador().getEmbarcacoes();
    }

    public boolean verificarCondicaoFim() {
        validarPartidaExistente();
        return partidaAtual.isFinalizada();
    }

    public void registrarVitoria(long totalSegundos) throws IOException {
        validarPartidaExistente();
        if (partidaAtual.getStatus() != StatusPartida.VITORIA_JOGADOR || vitoriaRegistrada) {
            return;
        }
        rankingController.adicionarPontuacao(partidaAtual.getNomeJogador(), totalSegundos);
        vitoriaRegistrada = true;
    }

    public List<JogadorInfo> obterRanking() {
        return rankingController.obterTop15();
    }

    public Partida getPartidaAtual() {
        return partidaAtual;
    }

    public void reiniciarPartida() {
        if (nomePartidaAtual == null) {
            throw new IllegalStateException("nenhuma partida iniciada");
        }
        iniciarNovaPartida(nomePartidaAtual, modoRandomAtual);
    }

    private void validarPartidaExistente() {
        if (partidaAtual == null) {
            throw new IllegalStateException("nenhuma partida iniciada");
        }
    }

    private void validarPartidaEmConfiguracao() {
        validarPartidaExistente();
        if (partidaAtual.getStatus() != StatusPartida.CONFIGURACAO) {
            throw new IllegalStateException("partida nao esta em configuracao");
        }
    }

    private boolean temEmbarcacaoDaClasse(Class<? extends Embarcacao> tipo) {
        return partidaAtual.getTabuleiroJogador().getEmbarcacoes().stream().anyMatch(tipo::isInstance);
    }

    private boolean possuiExatamenteUm(Tabuleiro tabuleiro, Class<? extends Embarcacao> tipo) {
        return tabuleiro.getEmbarcacoes().stream().filter(tipo::isInstance).count() == 1;
    }
}
