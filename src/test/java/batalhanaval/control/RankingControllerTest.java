package batalhanaval.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import batalhanaval.model.Ranking;

class RankingControllerTest {
    @TempDir
    Path tempDir;

    @Test
    void deveUsarClockFixoESalvarAposAdicionar() throws IOException {
        Path arquivo = tempDir.resolve("ranking.tsv");
        Clock clock = Clock.fixed(Instant.parse("2026-07-15T12:00:00Z"), ZoneId.of("UTC"));
        RankingController controller = new RankingController(new Ranking(), arquivo, clock);

        controller.adicionarPontuacao("Ana", 33);

        assertTrue(Files.exists(arquivo));
        assertEquals(LocalDateTime.of(2026, 7, 15, 12, 0), controller.obterTop15().get(0).dataPartida());
    }

    @Test
    void deveRecarregarEmNovaInstancia() throws IOException {
        Path arquivo = tempDir.resolve("ranking.tsv");
        Clock clock = Clock.fixed(Instant.parse("2026-07-15T12:00:00Z"), ZoneId.of("UTC"));
        RankingController primeiro = new RankingController(new Ranking(), arquivo, clock);
        primeiro.adicionarPontuacao("Ana", 33);
        RankingController segundo = new RankingController(new Ranking(), arquivo, clock);

        segundo.carregar();

        assertEquals("Ana", segundo.obterTop15().get(0).nome());
    }
}
