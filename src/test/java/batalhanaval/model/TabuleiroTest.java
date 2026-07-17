package batalhanaval.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

class TabuleiroTest {
    @Test
    void deveCriarGradeDezPorDezVazia() {
        Tabuleiro tabuleiro = new Tabuleiro();

        for (int linha = 0; linha < Tabuleiro.TAMANHO; linha++) {
            for (int coluna = 0; coluna < Tabuleiro.TAMANHO; coluna++) {
                Coordenada coordenada = new Coordenada(linha, coluna);
                assertEquals(StatusCelula.VAZIA, tabuleiro.getStatus(coordenada));
                assertFalse(tabuleiro.hasEmbarcacao(coordenada));
            }
        }
    }

    @Test
    void devePosicionarHorizontalEVertical() {
        Tabuleiro tabuleiro = new Tabuleiro();

        tabuleiro.posicionarEmbarcacao(new Submarino(), new Coordenada(0, 0), Orientacao.HORIZONTAL);
        tabuleiro.posicionarEmbarcacao(new NavioEscolta(), new Coordenada(2, 2), Orientacao.VERTICAL);

        assertTrue(tabuleiro.hasEmbarcacao(new Coordenada(0, 0)));
        assertTrue(tabuleiro.hasEmbarcacao(new Coordenada(0, 1)));
        assertTrue(tabuleiro.hasEmbarcacao(new Coordenada(2, 2)));
        assertTrue(tabuleiro.hasEmbarcacao(new Coordenada(3, 2)));
        assertTrue(tabuleiro.hasEmbarcacao(new Coordenada(4, 2)));
    }

    @Test
    void deveValidarLimitesNasQuatroBordas() {
        Tabuleiro tabuleiro = new Tabuleiro();

        assertTrue(tabuleiro.podePosicionar(new Submarino(), new Coordenada(0, 8), Orientacao.HORIZONTAL));
        assertFalse(tabuleiro.podePosicionar(new Submarino(), new Coordenada(0, 9), Orientacao.HORIZONTAL));
        assertTrue(tabuleiro.podePosicionar(new Submarino(), new Coordenada(8, 0), Orientacao.VERTICAL));
        assertFalse(tabuleiro.podePosicionar(new Submarino(), new Coordenada(9, 0), Orientacao.VERTICAL));
    }

    @Test
    void deveImpedirColisaoEPreservarAtomicidade() {
        Tabuleiro tabuleiro = new Tabuleiro();
        tabuleiro.posicionarEmbarcacao(new Submarino(), new Coordenada(0, 0), Orientacao.HORIZONTAL);

        assertThrows(IllegalStateException.class,
                () -> tabuleiro.posicionarEmbarcacao(new NavioEscolta(), new Coordenada(0, 1), Orientacao.HORIZONTAL));
        assertFalse(tabuleiro.hasEmbarcacao(new Coordenada(0, 2)));
        assertFalse(tabuleiro.hasEmbarcacao(new Coordenada(0, 3)));
        assertEquals(1, tabuleiro.getEmbarcacoes().size());
    }

    @Test
    void deveUsarMesmaInstanciaNasCelulasDaEmbarcacao() {
        Tabuleiro tabuleiro = new Tabuleiro();
        Caca caca = new Caca();
        tabuleiro.posicionarEmbarcacao(caca, new Coordenada(0, 0), Orientacao.HORIZONTAL);

        assertEquals(StatusTiro.ACERTO, tabuleiro.receberImpacto(new Coordenada(0, 0)));
        assertEquals(StatusTiro.AFUNDOU, tabuleiro.receberImpacto(new Coordenada(0, 1)));
        assertEquals(0, caca.getVida());
    }

    @Test
    void deveImpedirPosicionarMesmaInstanciaDuasVezes() {
        Tabuleiro tabuleiro = new Tabuleiro();
        Caca caca = new Caca();
        tabuleiro.posicionarEmbarcacao(caca, new Coordenada(0, 0), Orientacao.HORIZONTAL);

        assertFalse(tabuleiro.podePosicionar(caca, new Coordenada(2, 0), Orientacao.HORIZONTAL));
        assertThrows(IllegalStateException.class,
                () -> tabuleiro.posicionarEmbarcacao(caca, new Coordenada(2, 0), Orientacao.HORIZONTAL));
    }

    @Test
    void deveAplicarPadroesDosQuatroDisparosNoCentro() {
        assertCoordenadas(new Tabuleiro().disparar(new Coordenada(5, 5), TipoDisparo.SIMPLES),
                List.of(new Coordenada(5, 5)));
        assertCoordenadas(new Tabuleiro().disparar(new Coordenada(5, 5), TipoDisparo.CASCATA),
                List.of(new Coordenada(5, 5), new Coordenada(5, 6)));
        assertCoordenadas(new Tabuleiro().disparar(new Coordenada(5, 5), TipoDisparo.ESTRELA),
                List.of(new Coordenada(5, 5), new Coordenada(4, 5), new Coordenada(6, 5),
                        new Coordenada(5, 4), new Coordenada(5, 6)));
        assertCoordenadas(new Tabuleiro().disparar(new Coordenada(5, 5), TipoDisparo.PORTA_AVIOES),
                List.of(new Coordenada(5, 5)));
    }

    @Test
    void deveIgnorarCoordenadasForaDaGradeNosDisparosDeBorda() {
        assertCoordenadas(new Tabuleiro().disparar(new Coordenada(0, 9), TipoDisparo.CASCATA),
                List.of(new Coordenada(0, 9)));
        assertCoordenadas(new Tabuleiro().disparar(new Coordenada(0, 0), TipoDisparo.ESTRELA),
                List.of(new Coordenada(0, 0), new Coordenada(1, 0), new Coordenada(0, 1)));
    }

    @Test
    void tiroRepetidoNaoAplicaDanoNovamente() {
        Tabuleiro tabuleiro = new Tabuleiro();
        Submarino submarino = new Submarino();
        tabuleiro.posicionarEmbarcacao(submarino, new Coordenada(0, 0), Orientacao.HORIZONTAL);

        assertEquals(StatusTiro.ACERTO, tabuleiro.receberImpacto(new Coordenada(0, 0)));
        assertEquals(StatusTiro.REPETIDO, tabuleiro.receberImpacto(new Coordenada(0, 0)));
        assertEquals(1, submarino.getVida());
    }

    @Test
    void deveResponderDicaPositivaENegativa() {
        Tabuleiro tabuleiro = new Tabuleiro();
        tabuleiro.posicionarEmbarcacao(new Submarino(), new Coordenada(3, 4), Orientacao.HORIZONTAL);

        assertTrue(tabuleiro.temEmbarcacaoNaLinhaOuColuna(new Coordenada(3, 0)));
        assertTrue(tabuleiro.temEmbarcacaoNaLinhaOuColuna(new Coordenada(0, 4)));
        assertFalse(tabuleiro.temEmbarcacaoNaLinhaOuColuna(new Coordenada(0, 0)));
    }

    @Test
    void frotaVaziaNaoDeveEstarDestruida() {
        assertFalse(new Tabuleiro().isFrotaDestruida());
    }

    @Test
    void deveIdentificarFrotaCompletamenteDestruida() {
        Tabuleiro tabuleiro = new Tabuleiro();
        tabuleiro.posicionarEmbarcacao(new Submarino(), new Coordenada(0, 0), Orientacao.HORIZONTAL);

        tabuleiro.receberImpacto(new Coordenada(0, 0));
        tabuleiro.receberImpacto(new Coordenada(0, 1));

        assertTrue(tabuleiro.isFrotaDestruida());
    }

    @Test
    void devePosicionarFrotaPadraoAleatoriaComQuatroEmbarcacoesEOnzeCelulas() {
        Tabuleiro tabuleiro = new Tabuleiro();

        tabuleiro.posicionarFrotaPadraoAleatoria(new Random(7));

        assertEquals(4, tabuleiro.getEmbarcacoes().size());
        assertEquals(11, contarCelulasOcupadas(tabuleiro));
    }

    private void assertCoordenadas(List<ResultadoDisparo> resultados, List<Coordenada> esperadas) {
        assertEquals(esperadas, resultados.stream().map(ResultadoDisparo::coordenada).toList());
    }

    private int contarCelulasOcupadas(Tabuleiro tabuleiro) {
        int total = 0;
        for (int linha = 0; linha < Tabuleiro.TAMANHO; linha++) {
            for (int coluna = 0; coluna < Tabuleiro.TAMANHO; coluna++) {
                if (tabuleiro.hasEmbarcacao(new Coordenada(linha, coluna))) {
                    total++;
                }
            }
        }
        return total;
    }
}
