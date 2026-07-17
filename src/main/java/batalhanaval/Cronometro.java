package batalhanaval;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import java.time.LocalDateTime;

public class Cronometro extends Thread{
    
    private Instant end;
    private Instant start;
    private Duration timeElapsed;
    private JLabel tempo;
    private boolean status = true;
    private long seconds;
    private long minutes;
    private LocalDateTime data;
    
    public Cronometro (JLabel tempo)
    {
        this.tempo = tempo;
    }
    
    @Override
    public void run()
    {
        start = Instant.now();
        
        while(status)
        {
            try {
                end = Instant.now();
                data = LocalDateTime.now();
                timeElapsed = Duration.between(start, end);
                seconds = timeElapsed.getSeconds() % 60;
                minutes = timeElapsed.getSeconds() / 60;
                tempo.setText("Tempo total: " + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Cronometro.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        end = Instant.now();
        data = LocalDateTime.now();
        timeElapsed = Duration.between(start, end);
        seconds = timeElapsed.getSeconds() % 60;
        minutes = timeElapsed.getSeconds() / 60;
        tempo.setText("Tempo total: " + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
        
        
    }

    public long getSeconds() {
        return seconds;
    }

    public long getMinutes() {
        return minutes;
    }
    
    public long getTempo()
    {
        return timeElapsed.getSeconds();
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public LocalDateTime getData() {
        return data;
    }
}
