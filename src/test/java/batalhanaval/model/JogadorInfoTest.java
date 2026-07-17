package batalhanaval.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class JogadorInfoTest {
    @Test
    void deveCriarJogadorInfoValido() {
        LocalDateTime data = LocalDateTime.of(2026, 7, 15, 10, 0);

        JogadorInfo jogadorInfo = new JogadorInfo("Ana", 42, data);

        assertEquals("Ana", jogadorInfo.nome());
        assertEquals(42, jogadorInfo.totalSegundos());
        assertEquals(data, jogadorInfo.dataPartida());
    }

    @Test
    void deveValidarCamposObrigatorios() {
        LocalDateTime data = LocalDateTime.of(2026, 7, 15, 10, 0);

        assertThrows(IllegalArgumentException.class, () -> new JogadorInfo(null, 1, data));
        assertThrows(IllegalArgumentException.class, () -> new JogadorInfo(" ", 1, data));
        assertThrows(IllegalArgumentException.class, () -> new JogadorInfo("Ana", -1, data));
        assertThrows(NullPointerException.class, () -> new JogadorInfo("Ana", 1, null));
    }
}
