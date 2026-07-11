package batalhanaval;

import java.io.Serializable;
import java.time.LocalDateTime;


public class Info implements Serializable{
    private long seg, min, ttot;
    private String nome;
    private LocalDateTime data;
    
    public Info(String nome, long min, long seg, long ttot, LocalDateTime data)
    {
        this.nome = nome;
        this.seg = seg;
        this.min = min;
        this.ttot = ttot;
        this.data = data;
    }

    public long getSeg() {
        return seg;
    }

    public long getMin() {
        return min;
    }

    public String getNome() {
        return nome;
    }

    public long getTtot() {
        return ttot;
    }

    public LocalDateTime getData() {
        return data;
    }
    
}