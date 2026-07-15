package batalhanaval.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;

import org.junit.jupiter.api.Test;

class IAAdversariaTest {
    @Test
    void nuncaEscolheCelulaJaAtingida() {
        Tabuleiro tabuleiro = new Tabuleiro();
        tabuleiro.disparar(new Coordenada(0, 0), TipoDisparo.SIMPLES);
        IAAdversaria ia = new IAAdversaria(new RandomZero());

        Coordenada alvo = ia.calcularProximoAlvo(tabuleiro);

        assertFalse(new Coordenada(0, 0).equals(alvo));
        assertEquals(new Coordenada(0, 1), alvo);
    }

    @Test
    void podeEscolherLinhaEColunaNove() {
        Tabuleiro tabuleiro = new Tabuleiro();
        preencherExceto(tabuleiro, new Coordenada(9, 9));
        IAAdversaria ia = new IAAdversaria(new RandomZero());

        assertEquals(new Coordenada(9, 9), ia.calcularProximoAlvo(tabuleiro));
    }

    @Test
    void deveTerComportamentoDeterministicoComRandomControlado() {
        Tabuleiro tabuleiro = new Tabuleiro();
        IAAdversaria ia = new IAAdversaria(new RandomUltimo());

        assertEquals(new Coordenada(9, 9), ia.calcularProximoAlvo(tabuleiro));
    }

    @Test
    void deveFalharQuandoNaoHaAlvosDisponiveis() {
        Tabuleiro tabuleiro = new Tabuleiro();
        preencherExceto(tabuleiro, null);

        IAAdversaria ia = new IAAdversaria(new RandomZero());

        assertThrows(IllegalStateException.class, () -> ia.calcularProximoAlvo(tabuleiro));
    }

    private void preencherExceto(Tabuleiro tabuleiro, Coordenada excecao) {
        for (int linha = 0; linha < Tabuleiro.TAMANHO; linha++) {
            for (int coluna = 0; coluna < Tabuleiro.TAMANHO; coluna++) {
                Coordenada coordenada = new Coordenada(linha, coluna);
                if (!coordenada.equals(excecao)) {
                    tabuleiro.receberImpacto(coordenada);
                }
            }
        }
    }

    private static final class RandomZero extends Random {
        @Override
        public int nextInt(int bound) {
            return 0;
        }
    }

    private static final class RandomUltimo extends Random {
        @Override
        public int nextInt(int bound) {
            return bound - 1;
        }
    }
}
