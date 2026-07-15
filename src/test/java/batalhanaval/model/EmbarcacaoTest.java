package batalhanaval.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class EmbarcacaoTest {
    @Test
    void deveCriarPortaAvioesComNomeTamanhoEVidaInicial() {
        Embarcacao embarcacao = new PortaAvioes();

        assertEquals("Porta-aviões", embarcacao.getNome());
        assertEquals(4, embarcacao.getTamanho());
        assertEquals(4, embarcacao.getVida());
    }

    @Test
    void deveCriarNavioEscoltaComNomeTamanhoEVidaInicial() {
        Embarcacao embarcacao = new NavioEscolta();

        assertEquals("Navio de Escolta", embarcacao.getNome());
        assertEquals(3, embarcacao.getTamanho());
        assertEquals(3, embarcacao.getVida());
    }

    @Test
    void deveCriarSubmarinoComNomeTamanhoEVidaInicial() {
        Embarcacao embarcacao = new Submarino();

        assertEquals("Submarino", embarcacao.getNome());
        assertEquals(2, embarcacao.getTamanho());
        assertEquals(2, embarcacao.getVida());
    }

    @Test
    void deveCriarCacaComNomeTamanhoEVidaInicial() {
        Embarcacao embarcacao = new Caca();

        assertEquals("Caça", embarcacao.getNome());
        assertEquals(2, embarcacao.getTamanho());
        assertEquals(2, embarcacao.getVida());
    }

    @Test
    void deveAplicarDanoSemAfundarEnquantoAindaPossuiVida() {
        Embarcacao embarcacao = new NavioEscolta();

        embarcacao.aplicarDano();

        assertEquals(2, embarcacao.getVida());
        assertFalse(embarcacao.isAfundado());
    }

    @Test
    void deveAfundarQuandoVidaChegaAZero() {
        Embarcacao embarcacao = new Submarino();

        embarcacao.aplicarDano();
        embarcacao.aplicarDano();

        assertEquals(0, embarcacao.getVida());
        assertTrue(embarcacao.isAfundado());
    }

    @Test
    void danoAdicionalNaoDeveDeixarVidaNegativa() {
        Embarcacao embarcacao = new Caca();

        embarcacao.aplicarDano();
        embarcacao.aplicarDano();
        embarcacao.aplicarDano();

        assertEquals(0, embarcacao.getVida());
        assertTrue(embarcacao.isAfundado());
    }

    @Test
    void deveRejeitarNomeNulo() {
        assertThrows(IllegalArgumentException.class, () -> new EmbarcacaoTeste(null, 1));
    }

    @Test
    void deveRejeitarNomeVazio() {
        assertThrows(IllegalArgumentException.class, () -> new EmbarcacaoTeste(" ", 1));
    }

    @Test
    void deveRejeitarTamanhoNaoPositivo() {
        assertThrows(IllegalArgumentException.class, () -> new EmbarcacaoTeste("Teste", 0));
    }

    private static final class EmbarcacaoTeste extends Embarcacao {
        private EmbarcacaoTeste(String nome, int tamanho) {
            super(nome, tamanho);
        }
    }
}
