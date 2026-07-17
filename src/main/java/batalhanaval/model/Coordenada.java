package batalhanaval.model;

public record Coordenada(int linha, int coluna) {
    public Coordenada {
        validarIntervalo("linha", linha);
        validarIntervalo("coluna", coluna);
    }

    private static void validarIntervalo(String campo, int valor) {
        if (valor < 0 || valor > 9) {
            throw new IllegalArgumentException(campo + " deve estar entre 0 e 9");
        }
    }
}
