package batalhanaval.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import batalhanaval.control.JogoController;
import batalhanaval.control.RankingController;
import batalhanaval.model.JogadorInfo;
import batalhanaval.model.StatusPartida;

public class EndJanela extends JFrame {
    private final JogoController jogoController;
    private final RankingController rankingController;
    private final long totalSegundos;
    private final boolean vitoria;

    public EndJanela(
            JogoController jogoController,
            RankingController rankingController,
            long totalSegundos,
            boolean vitoria) {
        this.jogoController = jogoController;
        this.rankingController = rankingController;
        this.totalSegundos = totalSegundos;
        this.vitoria = vitoria;
        registrarSeVitoria();
        configurarJanela();
    }

    private void configurarJanela() {
        setTitle("Batalha Naval - Fim de jogo");
        RecursosImagens.aplicarIconeJanela(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(760, 620));
        setLayout(new BorderLayout(12, 12));

        add(criarTopo(), BorderLayout.NORTH);
        add(criarRanking(), BorderLayout.CENTER);
        add(criarBotoes(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel criarTopo() {
        JPanel painel = new JPanel(new GridLayout(3, 1, 4, 6));
        painel.setBorder(BorderFactory.createEmptyBorder(24, 24, 12, 24));
        painel.setBackground(vitoria ? new Color(30, 110, 82) : new Color(130, 62, 62));

        JLabel titulo = new JLabel(vitoria ? "Vitoria!" : "Derrota", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        painel.add(titulo);

        JLabel jogador = new JLabel("Jogador: " + jogoController.getNomeJogador(), SwingConstants.CENTER);
        jogador.setForeground(Color.WHITE);
        jogador.setFont(new Font("SansSerif", Font.BOLD, 18));
        painel.add(jogador);

        JLabel tempo = new JLabel("Tempo total: " + formatarTempo(totalSegundos), SwingConstants.CENTER);
        tempo.setForeground(Color.WHITE);
        tempo.setFont(new Font("SansSerif", Font.PLAIN, 18));
        painel.add(tempo);
        return painel;
    }

    private JPanel criarRanking() {
        JPanel painel = new JPanel(new GridLayout(16, 1, 2, 2));
        painel.setBorder(BorderFactory.createTitledBorder("Top 15"));
        JLabel cabecalho = new JLabel("Melhores tempos", SwingConstants.CENTER);
        cabecalho.setFont(new Font("SansSerif", Font.BOLD, 16));
        painel.add(cabecalho);

        List<JogadorInfo> jogadores = rankingController.obterTop15();
        for (int indice = 0; indice < 15; indice++) {
            JLabel linha = new JLabel("", SwingConstants.CENTER);
            if (indice < jogadores.size()) {
                JogadorInfo info = jogadores.get(indice);
                linha.setText((indice + 1) + ". " + info.nome() + " - " + formatarTempo(info.totalSegundos()));
            } else {
                linha.setText((indice + 1) + ". --");
            }
            painel.add(linha);
        }
        return painel;
    }

    private JPanel criarBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 18));
        JButton reiniciar = new JButton("Reiniciar");
        JButton novoJogo = new JButton("Novo jogo");
        JButton sair = new JButton("Sair");
        reiniciar.addActionListener(event -> reiniciar());
        novoJogo.addActionListener(event -> novoJogo());
        sair.addActionListener(event -> dispose());
        painel.add(reiniciar);
        painel.add(novoJogo);
        painel.add(sair);
        return painel;
    }

    private void registrarSeVitoria() {
        if (!vitoria) {
            return;
        }
        try {
            jogoController.registrarVitoria(totalSegundos);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "A vitoria foi concluida, mas o ranking nao pode ser salvo.",
                    "Ranking", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void reiniciar() {
        try {
            jogoController.reiniciarPartida();
            JFrame proxima = jogoController.getStatusPartida() == StatusPartida.CONFIGURACAO
                    ? new SetupJanela(jogoController, rankingController)
                    : new GameJanela(jogoController, rankingController);
            proxima.setVisible(true);
            dispose();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Reiniciar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void novoJogo() {
        new StartJanela(jogoController, rankingController).setVisible(true);
        dispose();
    }

    private String formatarTempo(long segundosTotais) {
        return String.format("%02d:%02d", segundosTotais / 60, segundosTotais % 60);
    }
}
