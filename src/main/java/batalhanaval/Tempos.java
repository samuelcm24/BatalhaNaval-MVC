package batalhanaval;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.time.LocalDateTime;


public class Tempos{
    private ArrayList<Info> rank;
    
    
    public Tempos()
    {
        rank = new ArrayList<Info>();
    }
        
    public Info[] retornaTops()
    {
    	Info[] tops = new Info[15];
        for(int i = 0; i<rank.size(); i++)
        {
            tops[i]=rank.get(i);
        }     
        return tops;
    }
    
    
    public void insere(String nome, long min, long sec, long tsec, LocalDateTime data)
    {
        int x = rank.size();
        if(x >= 15)
        {
            Info a;
            a = rank.get(x-1);
            if(a.getTtot() >= tsec)
            {
                rank.remove(x-1);
                rank.add(new Info(nome, min, sec, tsec, data));
            }
        }
        else
        {
            rank.add(new Info(nome, min, sec, tsec, data));
                
        }
        Collections.sort(rank, new CompareInfo());
        
    
    }
    
    public void ler()
    {
         try {
            rank = Arquivo.desserializar("tempos.dat");
        } catch (FileNotFoundException ex) {
            
        } catch (IOException ex) {
            
        } catch (ClassNotFoundException ex) { 
            
        }
    }
    
    public void escreve()
    { 
        try 
        {
            Arquivo.serializar("tempos.dat", rank);
        } 
        catch (IOException ex) 
        {
            
        }       
    }
    
    
}
