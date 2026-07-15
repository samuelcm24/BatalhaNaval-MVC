package batalhanaval.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import batalhanaval.control.JogoController;
import batalhanaval.control.RankingController;
import batalhanaval.model.Caca;
import batalhanaval.model.Embarcacao;
import batalhanaval.model.NavioEscolta;
import batalhanaval.model.Orientacao;
import batalhanaval.model.PortaAvioes;
import batalhanaval.model.StatusCelula;
import batalhanaval.model.Submarino;

public class SetupJanela extends JFrame {
    private static final int TAMANHO_CELULA = 52;

    private final JogoController jogoController;
    private final RankingController rankingController;
    private final JButton[][] celulas;
    private final Map<Class<? extends Embarcacao>, JRadioButton> opcoesEmbarcacao;
    private final Map<Class<? extends Embarcacao>, JLabel> statusEmbarcacao;
    private final JRadioButton horizontal;
    private final JRadioButton vertical;
    private final JButton iniciar;

    public SetupJanela(JogoController jogoController, RankingController rankingController) {
        this.jogoController = jogoController;
        this.rankingController = rankingController;
        celulas = new JButton[10][10];
        opcoesEmbarcacao = new LinkedHashMap<>();
        statusEmbarcacao = new LinkedHashMap<>();
        horizontal = new JRadioButton("Horizontal", true);
        vertical = new JRadioButton("Vertical");
        iniciar = new JButton("Iniciar partida");
        configurarJanela();
        atualizarVisual();
    }

    private void configurarJanela() {
        setTitle("Batalha Naval - Configuracao");
        RecursosImagens.aplicarIconeJanela(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(980, 720));
        setLayout(new BorderLayout(14, 14));

        JLabel titulo = new JLabel("Monte sua frota", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 30));
        titulo.setBorder(BorderFactory.createEmptyBorder(18, 18, 8, 18));
        add(titulo, BorderLayout.NORTH);

        add(criarPainelControles(), BorderLayout.WEST);
        add(criarTabuleiro(), BorderLayout.CENTER);

        iniciar.addActionListener(event -> iniciarPartida());
        ImageIcon startIcon = RecursosImagens.iconeInicio(140, 40);
        if (startIcon != null) {
            iniciar.setIcon(startIcon);
            iniciar.setText("");
        }
        JPanel rodape = new JPanel();
        rodape.add(iniciar);
        add(rodape, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel criarPainelControles() {
        JPanel painel = new JPanel(new GridLayout(0, 1, 6, 8));
        painel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 12));
        painel.setPreferredSize(new Dimension(270, 0));

        JLabel jogador = new JLabel("Jogador: " + jogoController.getNomeJogador());
        jogador.setFont(new Font("SansSerif", Font.BOLD, 16));
        painel.add(jogador);

        ButtonGroup grupoEmbarcacao = new ButtonGroup();
        adicionarOpcaoEmbarcacao(painel, grupoEmbarcacao, PortaAvioes.class, "Porta-avioes (4)");
        adicionarOpcaoEmbarcacao(painel, grupoEmbarcacao, NavioEscolta.class, "Navio de Escolta (3)");
        adicionarOpcaoEmbarcacao(painel, grupoEmbarcacao, Submarino.class, "Submarino (2)");
        adicionarOpcaoEmbarcacao(painel, grupoEmbarcacao, Caca.class, "Caca (2)");

        ButtonGroup grupoOrientacao = new ButtonGroup();
        grupoOrientacao.add(horizontal);
        grupoOrientacao.add(vertical);
        painel.add(new JLabel("Orientacao:"));
        painel.add(horizontal);
        painel.add(vertical);

        painel.add(new JLabel("Embarcacoes posicionadas:"));
        adicionarStatus(painel, PortaAvioes.class, "Porta-avioes");
        adicionarStatus(painel, NavioEscolta.class, "Navio de Escolta");
        adicionarStatus(painel, Submarino.class, "Submarino");
        adicionarStatus(painel, Caca.class, "Caca");
        return painel;
    }

    private void adicionarOpcaoEmbarcacao(
            JPanel painel,
            ButtonGroup grupo,
            Class<? extends Embarcacao> tipo,
            String texto) {
        JRadioButton opcao = new JRadioButton(texto);
        if (opcoesEmbarcacao.isEmpty()) {
            opcao.setSelected(true);
        }
        grupo.add(opcao);
        opcoesEmbarcacao.put(tipo, opcao);
        painel.add(opcao);
    }

    private void adicionarStatus(JPanel painel, Class<? extends Embarcacao> tipo, String texto) {
        JLabel label = new JLabel(texto + ": pendente");
        statusEmbarcacao.put(tipo, label);
        painel.add(label);
    }

    private JPanel criarTabuleiro() {
        JPanel painel = new JPanel(new GridLayout(11, 11, 3, 3));
        painel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 20));
        painel.add(new JLabel(""));
        for (int coluna = 0; coluna < 10; coluna++) {
            painel.add(new JLabel(String.valueOf(coluna + 1), SwingConstants.CENTER));
        }
        for (int linha = 0; linha < 10; linha++) {
            painel.add(new JLabel(String.valueOf((char) ('A' + linha)), SwingConstants.CENTER));
            for (int coluna = 0; coluna < 10; coluna++) {
                JButton botao = criarBotaoCelula(linha, coluna);
                celulas[linha][coluna] = botao;
                painel.add(botao);
            }
        }
        return painel;
    }

    private JButton criarBotaoCelula(int linha, int coluna) {
        JButton botao = new JButton();
        botao.setPreferredSize(new Dimension(TAMANHO_CELULA, TAMANHO_CELULA));
        botao.setToolTipText("Linha " + (char) ('A' + linha) + ", coluna " + (coluna + 1));
        botao.addActionListener(event -> posicionar(linha, coluna));
        return botao;
    }

    private void posicionar(int linha, int coluna) {
        Embarcacao embarcacao = novaEmbarcacaoSelecionada();
        if (embarcacao == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma embarcacao.", "Configuracao",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Orientacao orientacao = horizontal.isSelected() ? Orientacao.HORIZONTAL : Orientacao.VERTICAL;
        try {
            if (!jogoController.podePosicionarEmbarcacaoJogador(embarcacao, linha, coluna, orientacao)) {
                JOptionPane.showMessageDialog(this, "Posicao invalida para a embarcacao selecionada.",
                        "Configuracao", JOptionPane.WARNING_MESSAGE);
                return;
            }
            jogoController.posicionarEmbarcacaoJogador(embarcacao, linha, coluna, orientacao);
            JOptionPane.showMessageDialog(this, embarcacao.getNome() + " posicionada.", "Configuracao",
                    JOptionPane.INFORMATION_MESSAGE);
            atualizarVisual();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Configuracao", JOptionPane.WARNING_MESSAGE);
        }
    }

    private Embarcacao novaEmbarcacaoSelecionada() {
        if (opcoesEmbarcacao.get(PortaAvioes.class).isSelected()) {
            return new PortaAvioes();
        }
        if (opcoesEmbarcacao.get(NavioEscolta.class).isSelected()) {
            return new NavioEscolta();
        }
        if (opcoesEmbarcacao.get(Submarino.class).isSelected()) {
            return new Submarino();
        }
        if (opcoesEmbarcacao.get(Caca.class).isSelected()) {
            return new Caca();
        }
        return null;
    }

    private void iniciarPartida() {
        try {
            jogoController.iniciarPartidaConfigurada();
            new GameJanela(jogoController, rankingController).setVisible(true);
            dispose();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Posicione todas as embarcacoes antes de iniciar.",
                    "Configuracao incompleta", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void atualizarVisual() {
        for (int linha = 0; linha < 10; linha++) {
            for (int coluna = 0; coluna < 10; coluna++) {
                JButton botao = celulas[linha][coluna];
                StatusCelula status = jogoController.getStatusCelulaJogador(linha, coluna);
                boolean temEmbarcacao = jogoController.hasEmbarcacaoJogador(linha, coluna);
                botao.setBackground(temEmbarcacao ? new Color(72, 112, 145) : new Color(188, 225, 242));
                ImageIcon icone = temEmbarcacao ? iconeEmbarcacaoJogador(linha, coluna) : RecursosImagens.iconeAgua(TAMANHO_CELULA);
                aplicarIcone(botao, icone);
                botao.setText(status == StatusCelula.EMBARCACAO && icone == null ? "■" : "");
                botao.setForeground(Color.WHITE);
            }
        }

        for (Map.Entry<Class<? extends Embarcacao>, JRadioButton> entry : opcoesEmbarcacao.entrySet()) {
            boolean posicionada = jogoController.getEmbarcacoesJogador().stream().anyMatch(entry.getKey()::isInstance);
            entry.getValue().setEnabled(!posicionada);
            JLabel label = statusEmbarcacao.get(entry.getKey());
            label.setText(label.getText().split(":")[0] + (posicionada ? ": posicionada" : ": pendente"));
        }
        iniciar.setEnabled(jogoController.isConfiguracaoCompleta());
    }

    private ImageIcon iconeEmbarcacaoJogador(int linha, int coluna) {
        return jogoController.getNomeEmbarcacaoJogador(linha, coluna)
                .map(nome -> RecursosImagens.iconeEmbarcacao(
                        nome,
                        segmentoEmbarcacao(nome, linha, coluna),
                        isVertical(nome, linha, coluna),
                        TAMANHO_CELULA))
                .orElse(null);
    }

    private int segmentoEmbarcacao(String nome, int linha, int coluna) {
        if (isVertical(nome, linha, coluna)) {
            int inicio = linha;
            while (mesmaEmbarcacao(nome, inicio - 1, coluna)) {
                inicio--;
            }
            return linha - inicio + 1;
        }

        int inicio = coluna;
        while (mesmaEmbarcacao(nome, linha, inicio - 1)) {
            inicio--;
        }
        return coluna - inicio + 1;
    }

    private boolean isVertical(String nome, int linha, int coluna) {
        boolean horizontal = mesmaEmbarcacao(nome, linha, coluna - 1) || mesmaEmbarcacao(nome, linha, coluna + 1);
        if (horizontal) {
            return false;
        }
        return mesmaEmbarcacao(nome, linha - 1, coluna) || mesmaEmbarcacao(nome, linha + 1, coluna);
    }

    private boolean mesmaEmbarcacao(String nome, int linha, int coluna) {
        if (linha < 0 || linha >= 10 || coluna < 0 || coluna >= 10) {
            return false;
        }
        return jogoController.getNomeEmbarcacaoJogador(linha, coluna)
                .map(nome::equals)
                .orElse(false);
    }

    private void aplicarIcone(JButton botao, ImageIcon icone) {
        botao.setIcon(icone);
        botao.setDisabledIcon(icone);
    }
}
