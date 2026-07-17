package batalhanaval.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import batalhanaval.model.Caca;
import batalhanaval.model.Coordenada;
import batalhanaval.model.IAAdversaria;
import batalhanaval.model.JogadorInfo;
import batalhanaval.model.NavioEscolta;
import batalhanaval.model.Orientacao;
import batalhanaval.model.PortaAvioes;
import batalhanaval.model.ResultadoDisparo;
import batalhanaval.model.StatusCelula;
import batalhanaval.model.StatusPartida;
import batalhanaval.model.Submarino;
import batalhanaval.model.TipoDisparo;
import batalhanaval.model.Ranking;

class JogoControllerTest {
    @TempDir
    Path tempDir;

    @Test
    void deveCriarPartidaAleatoriaJaIniciada() {
        JogoController controller = controller();

        controller.iniciarNovaPartida("Ana", true);

        assertEquals(StatusPartida.EM_ANDAMENTO, controller.getPartidaAtual().getStatus());
        assertEquals(4, controller.getPartidaAtual().getTabuleiroJogador().getEmbarcacoes().size());
        assertEquals(4, controller.getPartidaAtual().getTabuleiroComputador().getEmbarcacoes().size());
    }

    @Test
    void deveCriarConfiguracaoManualComJogadorVazioEComputadorAleatorio() {
        JogoController controller = controller();

        controller.iniciarNovaPartida("Ana", false);

        assertEquals(StatusPartida.CONFIGURACAO, controller.getPartidaAtual().getStatus());
        assertTrue(controller.getPartidaAtual().getTabuleiroJogador().getEmbarcacoes().isEmpty());
        assertEquals(4, controller.getPartidaAtual().getTabuleiroComputador().getEmbarcacoes().size());
    }

    @Test
    void deveRejeitarEmbarcacaoDuplicadaEConfiguracaoIncompleta() {
        JogoController controller = controller();
        controller.iniciarNovaPartida("Ana", false);
        controller.posicionarEmbarcacaoJogador(new Submarino(), 0, 0, Orientacao.HORIZONTAL);

        assertFalse(controller.podePosicionarEmbarcacaoJogador(new Submarino(), 2, 0, Orientacao.HORIZONTAL));
        assertThrows(IllegalStateException.class,
                () -> controller.posicionarEmbarcacaoJogador(new Submarino(), 2, 0, Orientacao.HORIZONTAL));
        assertThrows(IllegalStateException.class, controller::iniciarPartidaConfigurada);
    }

    @Test
    void devePermitirPosicionamentoHorizontalEVerticalEIniciarAposFrotaCompleta() {
        JogoController controller = controller();
        controller.iniciarNovaPartida("Ana", false);

        posicionarFrotaManual(controller);
        controller.iniciarPartidaConfigurada();

        assertEquals(StatusPartida.EM_ANDAMENTO, controller.getPartidaAtual().getStatus());
        assertTrue(controller.getPartidaAtual().getTabuleiroJogador().hasEmbarcacao(new Coordenada(1, 0)));
        assertTrue(controller.getPartidaAtual().getTabuleiroJogador().hasEmbarcacao(new Coordenada(4, 2)));
    }

    @Test
    void deveSepararTiroDoJogadorERespostaAutomatica() {
        JogoController controller = partidaManualIniciada();

        assertEquals(1, controller.processarCliqueTiroJogador(9, 9, TipoDisparo.SIMPLES).size());
        Optional<ResultadoDisparo> resposta = controller.executarTurnoComputador();

        assertTrue(resposta.isPresent());
        assertEquals(new Coordenada(0, 0), resposta.get().coordenada());
    }

    @Test
    void iaNaoRepeteAlvoPeloController() {
        JogoController controller = partidaManualIniciada();
        Set<Coordenada> alvos = new HashSet<>();

        controller.processarCliqueTiroJogador(9, 9, TipoDisparo.SIMPLES);
        alvos.add(controller.executarTurnoComputador().orElseThrow().coordenada());
        controller.processarCliqueTiroJogador(9, 8, TipoDisparo.SIMPLES);
        alvos.add(controller.executarTurnoComputador().orElseThrow().coordenada());

        assertEquals(2, alvos.size());
    }

    @Test
    void fimDaPartidaNoTiroDoJogadorNaoExecutaTurnoExtraDoComputador() {
        JogoController controller = partidaManualIniciadaComBotDeAgua();
        afundarComputador(controller);

        assertTrue(controller.verificarCondicaoFim());
        assertEquals(Optional.empty(), controller.executarTurnoComputador());
    }

    @Test
    void deveDelegarTresDicasEDisponibilidadeDeDisparos() {
        JogoController controller = partidaManualIniciada();
        Coordenada dicaPositiva = primeiraCoordenadaComEmbarcacaoComputador(controller);

        assertTrue(controller.usarDica(dicaPositiva.linha(), dicaPositiva.coluna()));
        controller.usarDica(9, 9);
        controller.usarDica(8, 8);
        assertThrows(IllegalStateException.class, () -> controller.usarDica(7, 7));
        assertTrue(controller.podeUsarDisparo(TipoDisparo.SIMPLES));
        assertFalse(controller.podeUsarDisparo(TipoDisparo.PORTA_AVIOES));
    }

    @Test
    void deveExporConsultasDeLeituraParaView() {
        JogoController controller = partidaManualIniciada();

        assertEquals("Ana", controller.getNomeJogador());
        assertEquals(StatusPartida.EM_ANDAMENTO, controller.getStatusPartida());
        assertEquals(3, controller.getDicasRestantes());
        assertEquals(0, controller.getCargaPortaAvioes());
        assertEquals(4, controller.getEmbarcacoesJogador().size());
        assertTrue(controller.hasEmbarcacaoJogador(0, 0));
        assertFalse(controller.hasEmbarcacaoJogador(9, 9));
        assertEquals(Optional.of("Porta-aviões"), controller.getNomeEmbarcacaoJogador(0, 0));
        assertEquals(Optional.empty(), controller.getNomeEmbarcacaoJogador(9, 9));
        assertEquals(StatusCelula.EMBARCACAO, controller.getStatusCelulaJogador(0, 0));
        assertEquals(StatusCelula.VAZIA, controller.getStatusCelulaComputador(9, 9));
    }

    @Test
    void deveRegistrarVitoriaUmaUnicaVez() throws IOException {
        JogoController controller = partidaManualIniciadaComBotDeAgua();
        afundarComputador(controller);

        controller.registrarVitoria(44);
        controller.registrarVitoria(45);

        assertEquals(1, controller.obterRanking().size());
        assertEquals(44, controller.obterRanking().get(0).totalSegundos());
    }

    @Test
    void derrotaNaoDeveEntrarNoRanking() throws IOException {
        JogoController controller = partidaManualIniciadaComBotDeAtaque();
        afundarJogador(controller);

        controller.registrarVitoria(44);

        assertTrue(controller.obterRanking().isEmpty());
    }

    @Test
    void reiniciarPreservaNomeEModoComEstadoNovo() {
        JogoController controller = controller();
        controller.iniciarNovaPartida("Ana", false);
        posicionarFrotaManual(controller);
        controller.iniciarPartidaConfigurada();

        controller.reiniciarPartida();

        assertEquals("Ana", controller.getPartidaAtual().getNomeJogador());
        assertEquals(StatusPartida.CONFIGURACAO, controller.getPartidaAtual().getStatus());
        assertTrue(controller.getPartidaAtual().getTabuleiroJogador().getEmbarcacoes().isEmpty());
    }

    private void afundarComputador(JogoController controller) {
        for (int linha = 0; linha < 10 && !controller.verificarCondicaoFim(); linha++) {
            for (int coluna = 0; coluna < 10 && !controller.verificarCondicaoFim(); coluna++) {
                Coordenada alvo = new Coordenada(linha, coluna);
                if (controller.getPartidaAtual().getTabuleiroComputador().hasEmbarcacao(alvo)
                        && (controller.getPartidaAtual().getTabuleiroComputador().getStatus(alvo)
                                == StatusCelula.EMBARCACAO)) {
                    controller.processarCliqueTiroJogador(linha, coluna, TipoDisparo.SIMPLES);
                    if (!controller.verificarCondicaoFim()) {
                        controller.executarTurnoComputador();
                    }
                }
            }
        }
    }

    private void afundarJogador(JogoController controller) {
        controller.processarCliqueTiroJogador(9, 9, TipoDisparo.SIMPLES);
        while (!controller.verificarCondicaoFim()) {
            controller.executarTurnoComputador();
            if (!controller.verificarCondicaoFim()) {
                Coordenada alvo = proximoAlvoLivreComputador(controller);
                controller.processarCliqueTiroJogador(alvo.linha(), alvo.coluna(), primeiroDisparoDisponivel(controller));
            }
        }
    }

    private Coordenada proximoAlvoLivreComputador(JogoController controller) {
        for (int linha = 9; linha >= 0; linha--) {
            for (int coluna = 9; coluna >= 0; coluna--) {
                Coordenada coordenada = new Coordenada(linha, coluna);
                StatusCelula status = controller.getPartidaAtual().getTabuleiroComputador().getStatus(coordenada);
                if (status == StatusCelula.VAZIA || status == StatusCelula.EMBARCACAO) {
                    return coordenada;
                }
            }
        }
        throw new IllegalStateException("sem alvo");
    }

    private TipoDisparo primeiroDisparoDisponivel(JogoController controller) {
        for (TipoDisparo tipo : List.of(TipoDisparo.SIMPLES, TipoDisparo.CASCATA, TipoDisparo.ESTRELA,
                TipoDisparo.PORTA_AVIOES)) {
            if (controller.podeUsarDisparo(tipo)) {
                return tipo;
            }
        }
        throw new IllegalStateException("sem disparo disponivel");
    }

    private Coordenada primeiraCoordenadaComEmbarcacaoComputador(JogoController controller) {
        for (int linha = 0; linha < 10; linha++) {
            for (int coluna = 0; coluna < 10; coluna++) {
                Coordenada coordenada = new Coordenada(linha, coluna);
                if (controller.getPartidaAtual().getTabuleiroComputador().hasEmbarcacao(coordenada)) {
                    return coordenada;
                }
            }
        }
        throw new IllegalStateException("sem embarcacao");
    }

    private JogoController partidaManualIniciada() {
        JogoController controller = controller();
        controller.iniciarNovaPartida("Ana", false);
        posicionarFrotaManual(controller);
        controller.iniciarPartidaConfigurada();
        return controller;
    }

    private JogoController partidaManualIniciadaComBotDeAgua() {
        JogoController controller = controller(new SequenciaIA(
                new Coordenada(9, 9),
                new Coordenada(9, 8),
                new Coordenada(9, 7),
                new Coordenada(9, 6),
                new Coordenada(9, 5),
                new Coordenada(9, 4),
                new Coordenada(9, 3),
                new Coordenada(9, 2),
                new Coordenada(9, 1),
                new Coordenada(9, 0),
                new Coordenada(8, 9)));
        controller.iniciarNovaPartida("Ana", false);
        posicionarFrotaManual(controller);
        controller.iniciarPartidaConfigurada();
        return controller;
    }

    private JogoController partidaManualIniciadaComBotDeAtaque() {
        JogoController controller = controller(new SequenciaIA(
                new Coordenada(0, 0),
                new Coordenada(0, 1),
                new Coordenada(0, 2),
                new Coordenada(0, 3),
                new Coordenada(1, 0),
                new Coordenada(2, 0),
                new Coordenada(3, 0),
                new Coordenada(0, 7),
                new Coordenada(0, 8),
                new Coordenada(3, 2),
                new Coordenada(4, 2)));
        controller.iniciarNovaPartida("Ana", false);
        posicionarFrotaManual(controller);
        controller.iniciarPartidaConfigurada();
        return controller;
    }

    private void posicionarFrotaManual(JogoController controller) {
        controller.posicionarEmbarcacaoJogador(new PortaAvioes(), 0, 0, Orientacao.HORIZONTAL);
        controller.posicionarEmbarcacaoJogador(new NavioEscolta(), 1, 0, Orientacao.VERTICAL);
        controller.posicionarEmbarcacaoJogador(new Submarino(), 0, 7, Orientacao.HORIZONTAL);
        controller.posicionarEmbarcacaoJogador(new Caca(), 3, 2, Orientacao.VERTICAL);
    }

    private JogoController controller() {
        return controller(new IAAdversaria(new RandomZero()));
    }

    private JogoController controller(IAAdversaria ia) {
        Path arquivo = tempDir.resolve("ranking.tsv");
        RankingController rankingController = new RankingController(
                new Ranking(),
                arquivo,
                Clock.fixed(Instant.parse("2026-07-15T12:00:00Z"), ZoneId.of("UTC")));
        return new JogoController(ia, rankingController, new Random(7));
    }

    private static final class RandomZero extends Random {
        @Override
        public int nextInt(int bound) {
            return 0;
        }
    }

    private static final class SequenciaIA extends IAAdversaria {
        private final List<Coordenada> alvos;
        private int indice;

        private SequenciaIA(Coordenada... alvos) {
            super(new RandomZero());
            this.alvos = List.of(alvos);
        }

        @Override
        public Coordenada calcularProximoAlvo(batalhanaval.model.Tabuleiro tabuleiro) {
            return alvos.get(indice++);
        }
    }
}
