package batalhanaval.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class PartidaTest {
    @Test
    void deveValidarConstrucao() {
        Tabuleiro jogador = tabuleiroComFrotaPadrao();
        Tabuleiro computador = tabuleiroComFrotaPadrao();

        assertThrows(IllegalArgumentException.class, () -> new Partida("", jogador, computador));
        assertThrows(NullPointerException.class, () -> new Partida("Jogador", null, computador));
        assertThrows(NullPointerException.class, () -> new Partida("Jogador", jogador, null));
    }

    @Test
    void iniciarExigeFrotaEmAmbosTabuleiros() {
        Partida partida = new Partida("Jogador", new Tabuleiro(), tabuleiroComFrotaPadrao());

        assertThrows(IllegalStateException.class, partida::iniciar);
    }

    @Test
    void iniciarDefineStatusETurnoDoJogador() {
        Partida partida = partidaIniciada();

        assertEquals(StatusPartida.EM_ANDAMENTO, partida.getStatus());
        assertEquals(Turno.JOGADOR, partida.getTurno());
    }

    @Test
    void deveAlternarTurnosAposTirosValidos() {
        Partida partida = partidaIniciada();

        partida.processarTiroJogador(new Coordenada(9, 9), TipoDisparo.SIMPLES);
        assertEquals(Turno.COMPUTADOR, partida.getTurno());

        partida.processarTiroComputador(new Coordenada(9, 9));
        assertEquals(Turno.JOGADOR, partida.getTurno());
    }

    @Test
    void jogadorVenceAoDestruirFrotaDoComputador() {
        Tabuleiro jogador = tabuleiroComFrotaPadrao();
        Tabuleiro computador = new Tabuleiro();
        computador.posicionarEmbarcacao(new Submarino(), new Coordenada(0, 0), Orientacao.HORIZONTAL);
        Partida partida = new Partida("Jogador", jogador, computador);
        partida.iniciar();

        partida.processarTiroJogador(new Coordenada(0, 0), TipoDisparo.SIMPLES);
        partida.processarTiroComputador(new Coordenada(9, 9));
        partida.processarTiroJogador(new Coordenada(0, 1), TipoDisparo.SIMPLES);

        assertEquals(StatusPartida.VITORIA_JOGADOR, partida.getStatus());
        assertTrue(partida.isFinalizada());
    }

    @Test
    void computadorVenceAoDestruirFrotaDoJogador() {
        Tabuleiro jogador = new Tabuleiro();
        jogador.posicionarEmbarcacao(new Submarino(), new Coordenada(0, 0), Orientacao.HORIZONTAL);
        Tabuleiro computador = tabuleiroComFrotaPadrao();
        Partida partida = new Partida("Jogador", jogador, computador);
        partida.iniciar();

        partida.processarTiroJogador(new Coordenada(9, 9), TipoDisparo.SIMPLES);
        partida.processarTiroComputador(new Coordenada(0, 0));
        partida.processarTiroJogador(new Coordenada(9, 8), TipoDisparo.SIMPLES);
        partida.processarTiroComputador(new Coordenada(0, 1));

        assertEquals(StatusPartida.VITORIA_COMPUTADOR, partida.getStatus());
        assertTrue(partida.isFinalizada());
    }

    @Test
    void alvoRepetidoNaoTrocaTurno() {
        Partida partida = partidaIniciada();
        partida.processarTiroJogador(new Coordenada(9, 9), TipoDisparo.SIMPLES);
        partida.processarTiroComputador(new Coordenada(9, 9));

        assertThrows(IllegalStateException.class,
                () -> partida.processarTiroJogador(new Coordenada(9, 9), TipoDisparo.SIMPLES));
        assertEquals(Turno.JOGADOR, partida.getTurno());
    }

    @Test
    void devePermitirTresDicasEBloquearQuartaSemTrocarTurno() {
        Partida partida = partidaIniciada();

        assertTrue(partida.usarDica(new Coordenada(0, 9)));
        assertEquals(Turno.JOGADOR, partida.getTurno());
        partida.usarDica(new Coordenada(9, 9));
        partida.usarDica(new Coordenada(8, 8));

        assertEquals(0, partida.getDicasRestantes());
        assertThrows(IllegalStateException.class, () -> partida.usarDica(new Coordenada(7, 7)));
    }

    @Test
    void deveIndicarDisponibilidadeDeCadaDisparoConformeEmbarcacaoAtiva() {
        Partida partida = partidaIniciada();

        assertTrue(partida.podeUsarDisparo(TipoDisparo.SIMPLES));
        assertTrue(partida.podeUsarDisparo(TipoDisparo.CASCATA));
        assertTrue(partida.podeUsarDisparo(TipoDisparo.ESTRELA));
        assertFalse(partida.podeUsarDisparo(TipoDisparo.PORTA_AVIOES));

        afundarJogador(partida, new Coordenada(0, 7), new Coordenada(0, 8));
        assertFalse(partida.podeUsarDisparo(TipoDisparo.SIMPLES));
    }

    @Test
    void deveCarregarUsarEReiniciarDisparoPortaAvioes() {
        Partida partida = partidaIniciada();

        partida.processarTiroJogador(new Coordenada(9, 9), TipoDisparo.SIMPLES);
        assertEquals(1, partida.getCargaPortaAvioes());
        partida.processarTiroComputador(new Coordenada(9, 9));
        partida.processarTiroJogador(new Coordenada(9, 8), TipoDisparo.SIMPLES);
        assertEquals(2, partida.getCargaPortaAvioes());
        assertTrue(partida.podeUsarDisparo(TipoDisparo.PORTA_AVIOES));
        partida.processarTiroComputador(new Coordenada(9, 8));
        partida.processarTiroJogador(new Coordenada(9, 7), TipoDisparo.PORTA_AVIOES);

        assertEquals(0, partida.getCargaPortaAvioes());
    }

    @Test
    void disparoIndisponivelNaoAlteraEstado() {
        Partida partida = partidaIniciada();

        assertThrows(IllegalStateException.class,
                () -> partida.processarTiroJogador(new Coordenada(9, 9), TipoDisparo.PORTA_AVIOES));
        assertEquals(Turno.JOGADOR, partida.getTurno());
        assertEquals(0, partida.getCargaPortaAvioes());
        assertEquals(StatusCelula.VAZIA, partida.getTabuleiroComputador().getStatus(new Coordenada(9, 9)));
    }

    @Test
    void deveIntegrarPadroesEspeciaisAoTabuleiro() {
        Partida partida = partidaIniciada();

        List<ResultadoDisparo> resultados = partida.processarTiroJogador(new Coordenada(5, 5), TipoDisparo.ESTRELA);

        assertEquals(List.of(
                new Coordenada(5, 5),
                new Coordenada(4, 5),
                new Coordenada(6, 5),
                new Coordenada(5, 4),
                new Coordenada(5, 6)), resultados.stream().map(ResultadoDisparo::coordenada).toList());
        assertEquals(StatusCelula.AGUA_ATINGIDA, partida.getTabuleiroComputador().getStatus(new Coordenada(5, 5)));
    }

    private void afundarJogador(Partida partida, Coordenada primeira, Coordenada segunda) {
        partida.processarTiroJogador(new Coordenada(9, 9), TipoDisparo.SIMPLES);
        partida.processarTiroComputador(primeira);
        partida.processarTiroJogador(new Coordenada(9, 8), TipoDisparo.SIMPLES);
        partida.processarTiroComputador(segunda);
    }

    private Partida partidaIniciada() {
        Partida partida = new Partida("Jogador", tabuleiroComFrotaPadrao(), tabuleiroComFrotaPadrao());
        partida.iniciar();
        return partida;
    }

    private Tabuleiro tabuleiroComFrotaPadrao() {
        Tabuleiro tabuleiro = new Tabuleiro();
        tabuleiro.posicionarEmbarcacao(new PortaAvioes(), new Coordenada(0, 0), Orientacao.HORIZONTAL);
        tabuleiro.posicionarEmbarcacao(new NavioEscolta(), new Coordenada(0, 4), Orientacao.HORIZONTAL);
        tabuleiro.posicionarEmbarcacao(new Submarino(), new Coordenada(0, 7), Orientacao.HORIZONTAL);
        tabuleiro.posicionarEmbarcacao(new Caca(), new Coordenada(1, 0), Orientacao.HORIZONTAL);
        return tabuleiro;
    }
}
