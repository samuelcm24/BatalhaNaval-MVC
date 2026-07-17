package batalhanaval.model;

import java.util.Objects;

public record ResultadoDisparo(Coordenada coordenada, StatusTiro status) {
    public ResultadoDisparo {
        Objects.requireNonNull(coordenada, "coordenada nao pode ser nula");
        Objects.requireNonNull(status, "status nao pode ser nulo");
    }
}
