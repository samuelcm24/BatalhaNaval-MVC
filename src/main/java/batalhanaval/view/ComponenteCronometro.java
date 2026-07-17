package batalhanaval.view;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class ComponenteCronometro extends JLabel {
    private final Timer timer;
    private long segundosDecorridos;

    public ComponenteCronometro() {
        super("00:00");
        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(new Font("SansSerif", Font.BOLD, 18));
        timer = new Timer(1000, event -> {
            segundosDecorridos++;
            atualizarTexto();
        });
    }

    public void iniciar() {
        executarNaEdt(timer::start);
    }

    public void parar() {
        executarNaEdt(timer::stop);
    }

    public void reiniciar() {
        executarNaEdt(() -> {
            timer.stop();
            segundosDecorridos = 0;
            atualizarTexto();
            timer.start();
        });
    }

    public long getTempoTotalSegundos() {
        return segundosDecorridos;
    }

    private void atualizarTexto() {
        long minutos = segundosDecorridos / 60;
        long segundos = segundosDecorridos % 60;
        setText(String.format("%02d:%02d", minutos, segundos));
    }

    private void executarNaEdt(Runnable acao) {
        if (SwingUtilities.isEventDispatchThread()) {
            acao.run();
        } else {
            SwingUtilities.invokeLater(acao);
        }
    }
}
