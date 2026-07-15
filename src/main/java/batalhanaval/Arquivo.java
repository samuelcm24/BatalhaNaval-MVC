package batalhanaval;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;


public class Arquivo{
    
    public static ArrayList<Info> desserializar(String caminho)  throws ClassNotFoundException, FileNotFoundException, IOException {
        Object obj;
        FileInputStream arquivo = new FileInputStream(caminho);
        ObjectInputStream fluxo = new ObjectInputStream(arquivo);
        obj = fluxo.readObject();
        fluxo.close();
        return (ArrayList<Info>)obj;
    }   
    
    public static void serializar(String caminho, Object obj) throws FileNotFoundException, IOException {
        FileOutputStream arquivo = new FileOutputStream(caminho);
        ObjectOutputStream fluxo = new ObjectOutputStream(arquivo);
        fluxo.writeObject(obj);
        fluxo.close();
    }
   
}