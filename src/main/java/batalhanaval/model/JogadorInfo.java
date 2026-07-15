package batalhanaval.model;

import java.time.LocalDateTime;
import java.util.Objects;

public record JogadorInfo(String nome, long totalSegundos, LocalDateTime dataPartida) {
    public JogadorInfo {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("nome nao pode ser nulo ou vazio");
        }
        if (totalSegundos < 0) {
            throw new IllegalArgumentException("tempo total nao pode ser negativo");
        }
        Objects.requireNonNull(dataPartida, "dataPartida nao pode ser nula");
    }
}
