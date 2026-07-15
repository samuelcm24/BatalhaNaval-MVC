package batalhanaval.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class RankingTest {
    @TempDir
    Path tempDir;

    @Test
    void deveOrdenarPorTempoCrescente() {
        Ranking ranking = new Ranking();

        ranking.adicionarPontuacao(jogador("Lento", 20, 1));
        ranking.adicionarPontuacao(jogador("Rapido", 10, 1));

        assertEquals(List.of("Rapido", "Lento"), nomes(ranking.obterTop15()));
    }

    @Test
    void deveDesempatarPorDataENome() {
        Ranking ranking = new Ranking();

        ranking.adicionarPontuacao(new JogadorInfo("Bruno", 10, data(2)));
        ranking.adicionarPontuacao(new JogadorInfo("Ana", 10, data(2)));
        ranking.adicionarPontuacao(new JogadorInfo("Carlos", 10, data(1)));

        assertEquals(List.of("Carlos", "Ana", "Bruno"), nomes(ranking.obterTop15()));
    }

    @Test
    void deveManterSomenteTopQuinzeESubstituirPiorTempo() {
        Ranking ranking = new Ranking();
        for (int i = 0; i < 15; i++) {
            ranking.adicionarPontuacao(jogador("Jogador" + i, 100 + i, i + 1));
        }

        ranking.adicionarPontuacao(jogador("Novo", 50, 20));

        List<JogadorInfo> top = ranking.obterTop15();
        assertEquals(15, top.size());
        assertEquals("Novo", top.get(0).nome());
        assertFalse(nomes(top).contains("Jogador14"));
    }

    @Test
    void deveRetornarListaImutavel() {
        Ranking ranking = new Ranking();
        ranking.adicionarPontuacao(jogador("Ana", 10, 1));

        assertThrows(UnsupportedOperationException.class, () -> ranking.obterTop15().add(jogador("Bia", 11, 1)));
    }

    @Test
    void devePersistirNomesUtf8ERestaurarRoundTrip() throws IOException {
        Ranking ranking = new Ranking();
        Path arquivo = tempDir.resolve("ranking.tsv");
        ranking.adicionarPontuacao(new JogadorInfo("Joao Açu\tSeparador", 12, data(1)));

        ranking.salvar(arquivo);
        Ranking recarregado = new Ranking();
        recarregado.carregar(arquivo);

        assertEquals("Joao Açu\tSeparador", recarregado.obterTop15().get(0).nome());
        assertTrue(Files.readString(arquivo, StandardCharsets.UTF_8).contains(
                Base64.getEncoder().encodeToString("Joao Açu\tSeparador".getBytes(StandardCharsets.UTF_8))));
    }

    @Test
    void arquivoInexistenteGeraRankingVazio() throws IOException {
        Ranking ranking = new Ranking();

        ranking.carregar(tempDir.resolve("inexistente.tsv"));

        assertTrue(ranking.obterTop15().isEmpty());
    }

    @Test
    void deveIgnorarLinhasMalformadasEPreservarValidas() throws IOException {
        Path arquivo = tempDir.resolve("ranking.tsv");
        String nomeValido = Base64.getEncoder().encodeToString("Valido".getBytes(StandardCharsets.UTF_8));
        Files.writeString(arquivo, "# batalha-naval-ranking-v1\n\nmalformada\n" + nomeValido + "\t30\t" + data(1)
                + "\n@@@@\t1\t" + data(2), StandardCharsets.UTF_8);
        Ranking ranking = new Ranking();

        ranking.carregar(arquivo);

        assertEquals(List.of("Valido"), nomes(ranking.obterTop15()));
    }

    @Test
    void deveCriarDiretorioAutomaticamenteENaoDeixarTemporarioAposSalvar() throws IOException {
        Ranking ranking = new Ranking();
        ranking.adicionarPontuacao(jogador("Ana", 10, 1));
        Path diretorio = tempDir.resolve("novo").resolve("sub");
        Path arquivo = diretorio.resolve("ranking.tsv");

        ranking.salvar(arquivo);

        assertTrue(Files.exists(arquivo));
        try (var arquivos = Files.list(diretorio)) {
            assertEquals(List.of(arquivo), arquivos.toList());
        }
    }

    private JogadorInfo jogador(String nome, long totalSegundos, int dia) {
        return new JogadorInfo(nome, totalSegundos, data(dia));
    }

    private LocalDateTime data(int dia) {
        return LocalDateTime.of(2026, 7, dia, 10, 0);
    }

    private List<String> nomes(List<JogadorInfo> jogadores) {
        return jogadores.stream().map(JogadorInfo::nome).toList();
    }
}
