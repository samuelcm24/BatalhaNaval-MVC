package batalhanaval.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import batalhanaval.control.JogoController;
import batalhanaval.control.RankingController;

public class StartJanela extends JFrame {
    private final JogoController jogoController;
    private final RankingController rankingController;
    private final JTextField nomeCampo;

    public StartJanela(JogoController jogoController, RankingController rankingController) {
        this.jogoController = jogoController;
        this.rankingController = rankingController;
        nomeCampo = new JTextField(24);
        configurarJanela();
        setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Batalha Naval - Inicio");
        RecursosImagens.aplicarIconeJanela(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(720, 460));
        setLayout(new BorderLayout(16, 16));

        JPanel topo = new JPanel(new GridLayout(2, 1, 0, 8));
        topo.setBorder(BorderFactory.createEmptyBorder(36, 36, 12, 36));
        topo.setBackground(new Color(20, 45, 82));

        JLabel titulo = new JLabel("Batalha Naval", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 44));
        titulo.setForeground(Color.WHITE);
        topo.add(titulo);

        JLabel subtitulo = new JLabel("Escolha como montar sua frota para iniciar a partida.", SwingConstants.CENTER);
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 18));
        subtitulo.setForeground(new Color(220, 235, 250));
        topo.add(subtitulo);
        add(topo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 28));
        centro.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        centro.add(new JLabel("Nome do jogador:"));
        nomeCampo.setHorizontalAlignment(SwingConstants.CENTER);
        centro.add(nomeCampo);
        add(centro, BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 24));
        JButton aleatorio = new JButton("Partida aleatoria");
        JButton manual = new JButton("Configuracao manual");
        aleatorio.setPreferredSize(new Dimension(190, 42));
        manual.setPreferredSize(new Dimension(190, 42));
        aleatorio.addActionListener(event -> iniciar(true));
        manual.addActionListener(event -> iniciar(false));
        botoes.add(aleatorio);
        botoes.add(manual);
        add(botoes, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void iniciar(boolean modoRandom) {
        String nome = nomeCampo.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do jogador.", "Nome obrigatorio",
                    JOptionPane.WARNING_MESSAGE);
            nomeCampo.requestFocusInWindow();
            return;
        }

        try {
            jogoController.iniciarNovaPartida(nome, modoRandom);
            JFrame proxima = modoRandom
                    ? new GameJanela(jogoController, rankingController)
                    : new SetupJanela(jogoController, rankingController);
            proxima.setVisible(true);
            dispose();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Nao foi possivel iniciar",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
