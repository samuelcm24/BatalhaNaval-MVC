package batalhanaval.view;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

final class RecursosImagens {
    private static final String BASE = "/batalhanaval/imagens/";
    private static final Map<String, BufferedImage> ORIGINAIS = new HashMap<>();
    private static final Map<String, ImageIcon> ICONES = new HashMap<>();

    private RecursosImagens() {
    }

    static ImageIcon iconeAgua(int tamanho) {
        return icone("empty.png", tamanho);
    }

    static ImageIcon iconeErro(int tamanho) {
        return icone("vazio.png", tamanho);
    }

    static ImageIcon iconeAcerto(int tamanho) {
        return icone("hit.png", tamanho);
    }

    static ImageIcon iconeInicio(int largura, int altura) {
        return icone("start.png", largura, altura, false);
    }

    static ImageIcon iconeSair(int largura, int altura) {
        return icone("exitsign.png", largura, altura, false);
    }

    static ImageIcon iconeEmbarcacao(String nome, int segmento, boolean vertical, int tamanho) {
        String prefixo = prefixoArquivo(nome);
        if (prefixo == null) {
            return null;
        }
        return icone(prefixo + segmento + ".png", tamanho, tamanho, vertical);
    }

    static void aplicarIconeJanela(JFrame janela) {
        BufferedImage imagem = carregarOriginal("start.png");
        if (imagem != null) {
            janela.setIconImage(imagem);
        }
    }

    private static ImageIcon icone(String arquivo, int tamanho) {
        return icone(arquivo, tamanho, tamanho, false);
    }

    private static ImageIcon icone(String arquivo, int largura, int altura, boolean rotacionar) {
        String chave = arquivo + ":" + largura + "x" + altura + ":" + rotacionar;
        if (ICONES.containsKey(chave)) {
            return ICONES.get(chave);
        }

        BufferedImage original = carregarOriginal(arquivo);
        if (original == null) {
            ICONES.put(chave, null);
            return null;
        }
        BufferedImage base = rotacionar ? rotacionar(original) : original;
        ImageIcon icone = new ImageIcon(redimensionar(base, largura, altura));
        ICONES.put(chave, icone);
        return icone;
    }

    private static BufferedImage carregarOriginal(String arquivo) {
        if (ORIGINAIS.containsKey(arquivo)) {
            return ORIGINAIS.get(arquivo);
        }

        URL url = RecursosImagens.class.getResource(BASE + arquivo);
        if (url == null) {
            ORIGINAIS.put(arquivo, null);
            return null;
        }
        try {
            BufferedImage imagem = ImageIO.read(url);
            ORIGINAIS.put(arquivo, imagem);
            return imagem;
        } catch (IOException ex) {
            ORIGINAIS.put(arquivo, null);
            return null;
        }
    }

    private static Image redimensionar(BufferedImage imagem, int largura, int altura) {
        BufferedImage destino = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = destino.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            double escala = Math.min((double) largura / imagem.getWidth(), (double) altura / imagem.getHeight());
            int novaLargura = Math.max(1, (int) Math.round(imagem.getWidth() * escala));
            int novaAltura = Math.max(1, (int) Math.round(imagem.getHeight() * escala));
            int x = (largura - novaLargura) / 2;
            int y = (altura - novaAltura) / 2;
            g.drawImage(imagem, x, y, novaLargura, novaAltura, null);
        } finally {
            g.dispose();
        }
        return destino;
    }

    private static BufferedImage rotacionar(BufferedImage imagem) {
        BufferedImage destino = new BufferedImage(imagem.getHeight(), imagem.getWidth(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = destino.createGraphics();
        try {
            g.translate(destino.getWidth() / 2.0, destino.getHeight() / 2.0);
            g.rotate(Math.PI / 2);
            g.translate(-imagem.getWidth() / 2.0, -imagem.getHeight() / 2.0);
            g.drawImage(imagem, 0, 0, null);
        } finally {
            g.dispose();
        }
        return destino;
    }

    private static String prefixoArquivo(String nome) {
        return switch (nome) {
            case "Porta-aviões" -> "porta";
            case "Navio de Escolta" -> "escolta";
            case "Submarino" -> "sub";
            case "Caça" -> "caca";
            default -> null;
        };
    }
}
