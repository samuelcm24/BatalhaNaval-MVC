package batalhanaval.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CelulaTest {
    @Test
    void deveIniciarVaziaESemEmbarcacao() {
        Celula celula = new Celula();

        assertEquals(StatusCelula.VAZIA, celula.getStatus());
        assertFalse(celula.hasEmbarcacao());
    }

    @Test
    void devePosicionarEmbarcacaoEmCelulaVazia() {
        Celula celula = new Celula();

        celula.posicionarEmbarcacao(new Submarino());

        assertEquals(StatusCelula.EMBARCACAO, celula.getStatus());
        assertTrue(celula.hasEmbarcacao());
    }

    @Test
    void deveRejeitarEmbarcacaoNula() {
        Celula celula = new Celula();

        assertThrows(IllegalArgumentException.class, () -> celula.posicionarEmbarcacao(null));
    }

    @Test
    void deveRejeitarSobreposicaoDeEmbarcacao() {
        Celula celula = new Celula();
        celula.posicionarEmbarcacao(new Submarino());

        assertThrows(IllegalStateException.class, () -> celula.posicionarEmbarcacao(new Caca()));
    }

    @Test
    void deveReceberImpactoNaAgua() {
        Celula celula = new Celula();

        StatusTiro resultado = celula.receberImpacto();

        assertEquals(StatusTiro.AGUA, resultado);
        assertEquals(StatusCelula.AGUA_ATINGIDA, celula.getStatus());
    }

    @Test
    void deveRetornarRepetidoAoImpactarAguaNovamente() {
        Celula celula = new Celula();
        celula.receberImpacto();

        StatusTiro resultado = celula.receberImpacto();

        assertEquals(StatusTiro.REPETIDO, resultado);
        assertEquals(StatusCelula.AGUA_ATINGIDA, celula.getStatus());
    }

    @Test
    void deveImpactarEmbarcacaoEReduzirVida() {
        Celula celula = new Celula();
        Embarcacao submarino = new Submarino();
        celula.posicionarEmbarcacao(submarino);

        StatusTiro resultado = celula.receberImpacto();

        assertEquals(StatusTiro.ACERTO, resultado);
        assertEquals(StatusCelula.EMBARCACAO_ATINGIDA, celula.getStatus());
        assertEquals(1, submarino.getVida());
    }

    @Test
    void tiroRepetidoEmEmbarcacaoNaoAplicaDanoAdicional() {
        Celula celula = new Celula();
        Embarcacao submarino = new Submarino();
        celula.posicionarEmbarcacao(submarino);
        celula.receberImpacto();

        StatusTiro resultado = celula.receberImpacto();

        assertEquals(StatusTiro.REPETIDO, resultado);
        assertEquals(1, submarino.getVida());
    }

    @Test
    void deveAfundarEmbarcacaoCompartilhadaEntreDuasCelulas() {
        Caca caca = new Caca();
        Celula primeiraCelula = new Celula();
        Celula segundaCelula = new Celula();
        primeiraCelula.posicionarEmbarcacao(caca);
        segundaCelula.posicionarEmbarcacao(caca);

        StatusTiro primeiroResultado = primeiraCelula.receberImpacto();
        StatusTiro segundoResultado = segundaCelula.receberImpacto();

        assertEquals(StatusTiro.ACERTO, primeiroResultado);
        assertEquals(StatusTiro.AFUNDOU, segundoResultado);
        assertEquals(0, caca.getVida());
        assertTrue(caca.isAfundado());
    }

    @Test
    void deveManterEmbarcacaoDepoisDoImpacto() {
        Celula celula = new Celula();
        celula.posicionarEmbarcacao(new Submarino());

        celula.receberImpacto();

        assertTrue(celula.hasEmbarcacao());
    }
}
