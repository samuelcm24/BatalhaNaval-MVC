package batalhanaval.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Ranking {
    private static final int LIMITE = 15;
    private static final String VERSAO = "# batalha-naval-ranking-v1";
    private static final Comparator<JogadorInfo> ORDENACAO = Comparator
            .comparingLong(JogadorInfo::totalSegundos)
            .thenComparing(JogadorInfo::dataPartida)
            .thenComparing(JogadorInfo::nome);

    private final List<JogadorInfo> jogadores;

    public Ranking() {
        jogadores = new ArrayList<>();
    }

    public void adicionarPontuacao(JogadorInfo jogadorInfo) {
        jogadores.add(Objects.requireNonNull(jogadorInfo, "jogadorInfo nao pode ser nulo"));
        ordenarELimitar();
    }

    public List<JogadorInfo> obterTop15() {
        return List.copyOf(jogadores);
    }

    public void carregar(Path caminho) throws IOException {
        Objects.requireNonNull(caminho, "caminho nao pode ser nulo");
        jogadores.clear();
        if (!Files.exists(caminho)) {
            return;
        }

        for (String linha : Files.readAllLines(caminho, StandardCharsets.UTF_8)) {
            JogadorInfo jogadorInfo = parseLinha(linha);
            if (jogadorInfo != null) {
                jogadores.add(jogadorInfo);
            }
        }
        ordenarELimitar();
    }

    public void salvar(Path caminho) throws IOException {
        Objects.requireNonNull(caminho, "caminho nao pode ser nulo");
        Path diretorio = caminho.getParent();
        if (diretorio != null) {
            Files.createDirectories(diretorio);
        }

        Path temporario = diretorio == null
                ? Files.createTempFile("ranking-", ".tmp")
                : Files.createTempFile(diretorio, "ranking-", ".tmp");
        try {
            Files.write(temporario, linhasArquivo(), StandardCharsets.UTF_8);
            moverTemporario(temporario, caminho);
        } finally {
            Files.deleteIfExists(temporario);
        }
    }

    private List<String> linhasArquivo() {
        List<String> linhas = new ArrayList<>();
        linhas.add(VERSAO);
        for (JogadorInfo jogadorInfo : jogadores) {
            linhas.add(formatarLinha(jogadorInfo));
        }
        return linhas;
    }

    private String formatarLinha(JogadorInfo jogadorInfo) {
        String nome = Base64.getEncoder().encodeToString(jogadorInfo.nome().getBytes(StandardCharsets.UTF_8));
        return nome + "\t" + jogadorInfo.totalSegundos() + "\t" + jogadorInfo.dataPartida();
    }

    private JogadorInfo parseLinha(String linha) {
        if (linha == null || linha.isBlank() || linha.startsWith("#")) {
            return null;
        }
        String[] partes = linha.split("\t", -1);
        if (partes.length != 3) {
            return null;
        }
        try {
            String nome = new String(Base64.getDecoder().decode(partes[0]), StandardCharsets.UTF_8);
            long totalSegundos = Long.parseLong(partes[1]);
            LocalDateTime dataPartida = LocalDateTime.parse(partes[2]);
            return new JogadorInfo(nome, totalSegundos, dataPartida);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private void moverTemporario(Path temporario, Path destino) throws IOException {
        try {
            Files.move(temporario, destino, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException ex) {
            Files.move(temporario, destino, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void ordenarELimitar() {
        jogadores.sort(ORDENACAO);
        if (jogadores.size() > LIMITE) {
            jogadores.subList(LIMITE, jogadores.size()).clear();
        }
    }
}
