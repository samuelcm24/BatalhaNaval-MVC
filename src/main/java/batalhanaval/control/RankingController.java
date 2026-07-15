package batalhanaval.control;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import batalhanaval.model.JogadorInfo;
import batalhanaval.model.Ranking;

public class RankingController {
    private final Ranking rankingModel;
    private final Path arquivo;
    private final Clock clock;

    public RankingController() {
        this(
                new Ranking(),
                Path.of(System.getProperty("user.home"), ".batalha-naval", "ranking.tsv"),
                Clock.systemDefaultZone());
    }

    public RankingController(Ranking rankingModel, Path arquivo, Clock clock) {
        this.rankingModel = Objects.requireNonNull(rankingModel, "rankingModel nao pode ser nulo");
        this.arquivo = Objects.requireNonNull(arquivo, "arquivo nao pode ser nulo");
        this.clock = Objects.requireNonNull(clock, "clock nao pode ser nulo");
    }

    public void carregar() throws IOException {
        rankingModel.carregar(arquivo);
    }

    public void adicionarPontuacao(String nome, long totalSegundos) throws IOException {
        rankingModel.adicionarPontuacao(new JogadorInfo(nome, totalSegundos, LocalDateTime.now(clock)));
        rankingModel.salvar(arquivo);
    }

    public List<JogadorInfo> obterTop15() {
        return rankingModel.obterTop15();
    }
}
