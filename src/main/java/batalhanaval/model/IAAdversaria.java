package batalhanaval.model;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class IAAdversaria {
    private final Random random;

    public IAAdversaria() {
        this(new Random());
    }

    public IAAdversaria(Random random) {
        this.random = Objects.requireNonNull(random, "random nao pode ser nulo");
    }

    public Coordenada calcularProximoAlvo(Tabuleiro tabuleiro) {
        Objects.requireNonNull(tabuleiro, "tabuleiro nao pode ser nulo");
        List<Coordenada> disponiveis = tabuleiro.getCoordenadasDisponiveisParaTiro();
        if (disponiveis.isEmpty()) {
            throw new IllegalStateException("nao ha alvos disponiveis");
        }
        return disponiveis.get(random.nextInt(disponiveis.size()));
    }
}
