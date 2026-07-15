package batalhanaval.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CoordenadaTest {
    @Test
    void deveAceitarCoordenadasValidasNosLimites() {
        Coordenada origem = new Coordenada(0, 0);
        Coordenada limite = new Coordenada(9, 9);

        assertEquals(0, origem.linha());
        assertEquals(0, origem.coluna());
        assertEquals(9, limite.linha());
        assertEquals(9, limite.coluna());
    }

    @Test
    void deveRejeitarLinhaNegativa() {
        assertThrows(IllegalArgumentException.class, () -> new Coordenada(-1, 0));
    }

    @Test
    void deveRejeitarLinhaMaiorQueNove() {
        assertThrows(IllegalArgumentException.class, () -> new Coordenada(10, 0));
    }

    @Test
    void deveRejeitarColunaNegativa() {
        assertThrows(IllegalArgumentException.class, () -> new Coordenada(0, -1));
    }

    @Test
    void deveRejeitarColunaMaiorQueNove() {
        assertThrows(IllegalArgumentException.class, () -> new Coordenada(0, 10));
    }

    @Test
    void deveCompararRecordsComOsMesmosValores() {
        assertEquals(new Coordenada(4, 7), new Coordenada(4, 7));
    }
}
