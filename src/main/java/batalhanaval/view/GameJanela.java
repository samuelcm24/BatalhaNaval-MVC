package batalhanaval.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import javax.swing.Timer;

import batalhanaval.control.JogoController;
import batalhanaval.control.RankingController;
import batalhanaval.model.ResultadoDisparo;
import batalhanaval.model.StatusCelula;
import batalhanaval.model.StatusPartida;
import batalhanaval.model.StatusTiro;
import batalhanaval.model.TipoDisparo;

public class GameJanela extends JFrame {
    private static final Color COR_AGUA = new Color(188, 225, 242);
    private static final Color COR_NAVIO = new Color(72, 112, 145);
    private static final Color COR_ERRO = new Color(215, 232, 238);
    private static final Color COR_ACERTO = new Color(205, 73, 73);
    private static final int TAMANHO_CELULA = 42;

    private final JogoController jogoController;
    private final RankingController rankingController;
    private final JButton[][] tabuleiroJogador;
    private final JButton[][] tabuleiroComputador;
    private final Map<TipoDisparo, JRadioButton> disparos;
    private final JRadioButton dica;
    private final JLabel mensagem;
    private final ComponenteCronometro cronometro;
    private Timer turnoComputadorTimer;
    private boolean aguardandoComputador;
    private boolean encerrada;
    private boolean fimAberto;

    public GameJanela(JogoController jogoController, RankingController rankingController) {
        this.jogoController = jogoController;
        this.rankingController = rankingController;
        tabuleiroJogador = new JButton[10][10];
        tabuleiroComputador = new JButton[10][10];
        disparos = new EnumMap<>(TipoDisparo.class);
        dica = new JRadioButton("Dica");
        mensagem = new JLabel("Sua vez: escolha um disparo e selecione o tabuleiro adversario.");
        cronometro = new ComponenteCronometro();
        configurarJanela();
        atualizarTudo();
        cronometro.iniciar();
    }

    private void configurarJanela() {
        setTitle("Batalha Naval - Jogo");
        RecursosImagens.aplicarIconeJanela(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1180, 760));
        setLayout(new BorderLayout(12, 12));

        add(criarTopo(), BorderLayout.NORTH);
        add(criarCentro(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel criarTopo() {
        JPanel topo = new JPanel(new BorderLayout(12, 8));
        topo.setBorder(BorderFactory.createEmptyBorder(14, 18, 8, 18));

        JLabel titulo = new JLabel("Batalha Naval");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        topo.add(titulo, BorderLayout.WEST);

        JPanel info = new JPanel(new GridLayout(2, 1));
        info.add(new JLabel("Jogador: " + jogoController.getNomeJogador(), SwingConstants.CENTER));
        info.add(cronometro);
        topo.add(info, BorderLayout.CENTER);

        JButton sair = new JButton("Encerrar");
        ImageIcon sairIcon = RecursosImagens.iconeSair(96, 30);
        if (sairIcon != null) {
            sair.setIcon(sairIcon);
            sair.setText("");
        }
        sair.addActionListener(event -> abrirFim(false));
        topo.add(sair, BorderLayout.EAST);
        return topo;
    }

    private JPanel criarCentro() {
        JPanel centro = new JPanel(new GridLayout(1, 3, 12, 0));
        centro.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        centro.add(criarPainelTabuleiro("Seu tabuleiro", tabuleiroJogador, false));
        centro.add(criarPainelControles());
        centro.add(criarPainelTabuleiro("Adversario", tabuleiroComputador, true));
        return centro;
    }

    private JPanel criarPainelControles() {
        JPanel painel = new JPanel(new GridLayout(0, 1, 6, 8));
        painel.setBorder(BorderFactory.createTitledBorder("Acoes"));

        ButtonGroup grupo = new ButtonGroup();
        adicionarDisparo(painel, grupo, TipoDisparo.SIMPLES, "Disparo simples");
        adicionarDisparo(painel, grupo, TipoDisparo.CASCATA, "Disparo cascata");
        adicionarDisparo(painel, grupo, TipoDisparo.ESTRELA, "Disparo estrela");
        adicionarDisparo(painel, grupo, TipoDisparo.PORTA_AVIOES, "Porta-avioes");
        grupo.add(dica);
        painel.add(dica);

        painel.add(new JLabel("Legenda:"));
        painel.add(legenda("Agua", COR_AGUA));
        painel.add(legenda("Embarcacao", COR_NAVIO));
        painel.add(legenda("Erro", COR_ERRO));
        painel.add(legenda("Acerto", COR_ACERTO));
        return painel;
    }

    private void adicionarDisparo(JPanel painel, ButtonGroup grupo, TipoDisparo tipo, String texto) {
        JRadioButton botao = new JRadioButton(texto);
        if (disparos.isEmpty()) {
            botao.setSelected(true);
        }
        grupo.add(botao);
        disparos.put(tipo, botao);
        painel.add(botao);
    }

    private JLabel legenda(String texto, Color cor) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(cor);
        label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        return label;
    }

    private JPanel criarPainelTabuleiro(String titulo, JButton[][] botoes, boolean adversario) {
        JPanel externo = new JPanel(new BorderLayout(6, 6));
        externo.setBorder(BorderFactory.createTitledBorder(titulo));
        JPanel grade = new JPanel(new GridLayout(11, 11, 3, 3));
        grade.add(new JLabel(""));
        for (int coluna = 0; coluna < 10; coluna++) {
            grade.add(new JLabel(String.valueOf(coluna + 1), SwingConstants.CENTER));
        }
        for (int linha = 0; linha < 10; linha++) {
            grade.add(new JLabel(String.valueOf((char) ('A' + linha)), SwingConstants.CENTER));
            for (int coluna = 0; coluna < 10; coluna++) {
                JButton botao = new JButton();
                botao.setPreferredSize(new Dimension(TAMANHO_CELULA, TAMANHO_CELULA));
                botao.setToolTipText("Linha " + (char) ('A' + linha) + ", coluna " + (coluna + 1));
                botao.setFocusPainted(false);
                if (adversario) {
                    int l = linha;
                    int c = coluna;
                    botao.addActionListener(event -> cliqueAdversario(l, c));
                } else {
                    botao.setEnabled(false);
                }
                botoes[linha][coluna] = botao;
                grade.add(botao);
            }
        }
        externo.add(grade, BorderLayout.CENTER);
        return externo;
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        mensagem.setFont(new Font("SansSerif", Font.BOLD, 14));
        rodape.add(mensagem);
        return rodape;
    }

    private void cliqueAdversario(int linha, int coluna) {
        if (aguardandoComputador || encerrada || fimAberto) {
            return;
        }
        if (dica.isSelected()) {
            usarDica(linha, coluna);
            return;
        }

        TipoDisparo tipo = disparoSelecionado();
        if (tipo == null) {
            JOptionPane.showMessageDialog(this, "Selecione um disparo.", "Jogada", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!jogoController.podeUsarDisparo(tipo)) {
            JOptionPane.showMessageDialog(this, "Disparo indisponivel no momento.", "Jogada invalida",
                    JOptionPane.WARNING_MESSAGE);
            atualizarTudo();
            return;
        }

        try {
            List<ResultadoDisparo> resultados = jogoController.processarCliqueTiroJogador(linha, coluna, tipo);
            mensagem.setText("Seu disparo: " + resumo(resultados));
            atualizarTudo();
            if (jogoController.verificarCondicaoFim()) {
                abrirFim(jogoController.getStatusPartida() == StatusPartida.VITORIA_JOGADOR);
                return;
            }
            iniciarTurnoComputador();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Jogada invalida", JOptionPane.WARNING_MESSAGE);
            atualizarTudo();
        }
    }

    private void usarDica(int linha, int coluna) {
        try {
            boolean existe = jogoController.usarDica(linha, coluna);
            mensagem.setText(existe
                    ? "Dica: existe embarcacao nessa linha ou coluna."
                    : "Dica: nao ha embarcacao nessa linha ou coluna.");
            JOptionPane.showMessageDialog(this, mensagem.getText(), "Dica", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Dica indisponivel", JOptionPane.WARNING_MESSAGE);
        }
        atualizarTudo();
    }

    private void iniciarTurnoComputador() {
        cancelarTurnoComputadorPendente();
        aguardandoComputador = true;
        atualizarHabilitacaoTabuleiroComputador(false);
        mensagem.setText("Turno do computador...");
        turnoComputadorTimer = new Timer(550, event -> {
            ((Timer) event.getSource()).stop();
            turnoComputadorTimer = null;
            if (encerrada || fimAberto || !isDisplayable()) {
                return;
            }
            executarTurnoComputador();
        });
        turnoComputadorTimer.setRepeats(false);
        turnoComputadorTimer.start();
    }

    private void executarTurnoComputador() {
        if (encerrada || fimAberto) {
            return;
        }
        try {
            Optional<ResultadoDisparo> resultado = jogoController.executarTurnoComputador();
            resultado.ifPresent(disparo -> mensagem.setText("Computador atacou "
                    + nomeCoordenada(disparo.coordenada().linha(), disparo.coordenada().coluna())
                    + ": " + textoStatus(disparo.status()) + "."));
            atualizarTudo();
            if (jogoController.verificarCondicaoFim()) {
                abrirFim(jogoController.getStatusPartida() == StatusPartida.VITORIA_JOGADOR);
            }
        } catch (RuntimeException ex) {
            if (!encerrada && !fimAberto) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Turno do computador", JOptionPane.ERROR_MESSAGE);
            }
        } finally {
            aguardandoComputador = false;
            if (!encerrada && !fimAberto) {
                atualizarTudo();
            }
        }
    }

    private TipoDisparo disparoSelecionado() {
        for (Map.Entry<TipoDisparo, JRadioButton> entry : disparos.entrySet()) {
            if (entry.getValue().isSelected()) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void atualizarTudo() {
        atualizarTabuleiroJogador();
        atualizarTabuleiroComputador();
        atualizarDisparos();
        dica.setText("Dica (" + jogoController.getDicasRestantes() + ")");
        dica.setEnabled(jogoController.getDicasRestantes() > 0 && !aguardandoComputador);
        atualizarHabilitacaoTabuleiroComputador(!aguardandoComputador && !jogoController.verificarCondicaoFim());
    }

    private void atualizarTabuleiroJogador() {
        for (int linha = 0; linha < 10; linha++) {
            for (int coluna = 0; coluna < 10; coluna++) {
                JButton botao = tabuleiroJogador[linha][coluna];
                StatusCelula status = jogoController.getStatusCelulaJogador(linha, coluna);
                aplicarVisualJogador(botao, status, linha, coluna);
            }
        }
    }

    private void atualizarTabuleiroComputador() {
        for (int linha = 0; linha < 10; linha++) {
            for (int coluna = 0; coluna < 10; coluna++) {
                JButton botao = tabuleiroComputador[linha][coluna];
                StatusCelula status = jogoController.getStatusCelulaComputador(linha, coluna);
                aplicarVisualComputador(botao, status);
            }
        }
    }

    private void aplicarVisualJogador(JButton botao, StatusCelula status, int linha, int coluna) {
        boolean temEmbarcacao = jogoController.hasEmbarcacaoJogador(linha, coluna);
        ImageIcon icone = temEmbarcacao ? iconeEmbarcacaoJogador(linha, coluna) : null;
        aplicarVisual(botao, status, temEmbarcacao, icone, false);
    }

    private void aplicarVisualComputador(JButton botao, StatusCelula status) {
        aplicarVisual(botao, status, false, null, true);
    }

    private void aplicarVisual(
            JButton botao,
            StatusCelula status,
            boolean mostrarEmbarcacao,
            ImageIcon iconeEmbarcacao,
            boolean adversario) {
        botao.setOpaque(true);
        botao.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        botao.setForeground(Color.WHITE);
        botao.setText("");
        aplicarIcone(botao, null);

        if (status == StatusCelula.AGUA_ATINGIDA) {
            botao.setBackground(COR_ERRO);
            botao.setForeground(Color.DARK_GRAY);
            ImageIcon icone = RecursosImagens.iconeErro(TAMANHO_CELULA);
            aplicarIcone(botao, icone);
            botao.setText(icone == null ? "~" : "");
            botao.setToolTipText("Agua atingida");
        } else if (status == StatusCelula.EMBARCACAO_ATINGIDA) {
            botao.setBackground(COR_ACERTO);
            ImageIcon icone = RecursosImagens.iconeAcerto(TAMANHO_CELULA);
            aplicarIcone(botao, icone);
            botao.setText(icone == null ? "X" : "");
            botao.setToolTipText("Embarcacao atingida");
        } else if (mostrarEmbarcacao) {
            botao.setBackground(COR_NAVIO);
            aplicarIcone(botao, iconeEmbarcacao);
            botao.setText(iconeEmbarcacao == null ? "■" : "");
            botao.setToolTipText(adversario ? "Embarcacao revelada" : "Sua embarcacao");
        } else {
            botao.setBackground(COR_AGUA);
            botao.setForeground(Color.DARK_GRAY);
            aplicarIcone(botao, RecursosImagens.iconeAgua(TAMANHO_CELULA));
            botao.setToolTipText("Agua");
        }
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

    private void atualizarDisparos() {
        for (Map.Entry<TipoDisparo, JRadioButton> entry : disparos.entrySet()) {
            boolean disponivel = jogoController.podeUsarDisparo(entry.getKey()) && !aguardandoComputador;
            entry.getValue().setEnabled(disponivel);
            if (entry.getValue().isSelected() && !disponivel) {
                selecionarPrimeiroDisparoDisponivel();
            }
        }
    }

    private void selecionarPrimeiroDisparoDisponivel() {
        for (Map.Entry<TipoDisparo, JRadioButton> entry : disparos.entrySet()) {
            if (jogoController.podeUsarDisparo(entry.getKey())) {
                entry.getValue().setSelected(true);
                return;
            }
        }
        dica.setSelected(true);
    }

    private void atualizarHabilitacaoTabuleiroComputador(boolean habilitado) {
        for (JButton[] linha : tabuleiroComputador) {
            for (JButton botao : linha) {
                botao.setEnabled(habilitado);
            }
        }
    }

    private String resumo(List<ResultadoDisparo> resultados) {
        return resultados.stream()
                .map(resultado -> nomeCoordenada(resultado.coordenada().linha(), resultado.coordenada().coluna())
                        + " " + textoStatus(resultado.status()))
                .toList()
                .toString();
    }

    private String textoStatus(StatusTiro status) {
        return switch (status) {
            case AGUA -> "agua";
            case ACERTO -> "acerto";
            case AFUNDOU -> "afundou";
            case REPETIDO -> "repetido";
        };
    }

    private String nomeCoordenada(int linha, int coluna) {
        return String.valueOf((char) ('A' + linha)) + (coluna + 1);
    }

    private void abrirFim(boolean vitoria) {
        if (fimAberto) {
            return;
        }
        fimAberto = true;
        cancelarTurnoComputadorPendente();
        new EndJanela(jogoController, rankingController, cronometro.getTempoTotalSegundos(), vitoria).setVisible(true);
        dispose();
    }

    private void cancelarTurnoComputadorPendente() {
        if (turnoComputadorTimer != null) {
            turnoComputadorTimer.stop();
            turnoComputadorTimer = null;
        }
        aguardandoComputador = false;
    }

    @Override
    public void dispose() {
        encerrada = true;
        cancelarTurnoComputadorPendente();
        cronometro.parar();
        super.dispose();
    }
}
