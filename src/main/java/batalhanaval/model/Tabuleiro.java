package batalhanaval.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class Tabuleiro {
    public static final int TAMANHO = 10;

    private final Celula[][] grade;
    private final List<Embarcacao> embarcacoes;

    public Tabuleiro() {
        grade = new Celula[TAMANHO][TAMANHO];
        embarcacoes = new ArrayList<>();
        for (int linha = 0; linha < TAMANHO; linha++) {
            for (int coluna = 0; coluna < TAMANHO; coluna++) {
                grade[linha][coluna] = new Celula();
            }
        }
    }

    public StatusCelula getStatus(Coordenada coordenada) {
        return celulaEm(coordenada).getStatus();
    }

    public boolean hasEmbarcacao(Coordenada coordenada) {
        return celulaEm(coordenada).hasEmbarcacao();
    }

    public Optional<String> getNomeEmbarcacao(Coordenada coordenada) {
        return celulaEm(coordenada).getNomeEmbarcacao();
    }

    public List<Embarcacao> getEmbarcacoes() {
        return List.copyOf(embarcacoes);
    }

    public boolean temFrota() {
        return !embarcacoes.isEmpty();
    }

    public boolean temEmbarcacaoAtiva(Class<? extends Embarcacao> tipo) {
        Objects.requireNonNull(tipo, "tipo nao pode ser nulo");
        for (Embarcacao embarcacao : embarcacoes) {
            if (tipo.isInstance(embarcacao) && !embarcacao.isAfundado()) {
                return true;
            }
        }
        return false;
    }

    public boolean podePosicionar(Embarcacao embarcacao, Coordenada inicio, Orientacao orientacao) {
        Objects.requireNonNull(embarcacao, "embarcacao nao pode ser nula");
        Objects.requireNonNull(inicio, "inicio nao pode ser nulo");
        Objects.requireNonNull(orientacao, "orientacao nao pode ser nula");

        if (embarcacoes.contains(embarcacao)) {
            return false;
        }

        List<Coordenada> coordenadas = calcularCoordenadas(embarcacao, inicio, orientacao);
        if (coordenadas.size() != embarcacao.getTamanho()) {
            return false;
        }
        for (Coordenada coordenada : coordenadas) {
            if (celulaEm(coordenada).hasEmbarcacao()) {
                return false;
            }
        }
        return true;
    }

    public void posicionarEmbarcacao(Embarcacao embarcacao, Coordenada inicio, Orientacao orientacao) {
        if (!podePosicionar(embarcacao, inicio, orientacao)) {
            throw new IllegalStateException("nao e possivel posicionar a embarcacao");
        }

        List<Coordenada> coordenadas = calcularCoordenadas(embarcacao, inicio, orientacao);
        for (Coordenada coordenada : coordenadas) {
            celulaEm(coordenada).posicionarEmbarcacao(embarcacao);
        }
        embarcacoes.add(embarcacao);
    }

    public StatusTiro receberImpacto(Coordenada coordenada) {
        return celulaEm(coordenada).receberImpacto();
    }

    public List<ResultadoDisparo> disparar(Coordenada alvo, TipoDisparo tipo) {
        Objects.requireNonNull(alvo, "alvo nao pode ser nulo");
        Objects.requireNonNull(tipo, "tipo nao pode ser nulo");

        List<ResultadoDisparo> resultados = new ArrayList<>();
        for (Coordenada coordenada : coordenadasDoDisparo(alvo, tipo)) {
            resultados.add(new ResultadoDisparo(coordenada, receberImpacto(coordenada)));
        }
        return List.copyOf(resultados);
    }

    public boolean temEmbarcacaoNaLinhaOuColuna(Coordenada coordenada) {
        Objects.requireNonNull(coordenada, "coordenada nao pode ser nula");
        for (int indice = 0; indice < TAMANHO; indice++) {
            if (grade[coordenada.linha()][indice].hasEmbarcacao()
                    || grade[indice][coordenada.coluna()].hasEmbarcacao()) {
                return true;
            }
        }
        return false;
    }

    public boolean isFrotaDestruida() {
        return temFrota() && embarcacoes.stream().allMatch(Embarcacao::isAfundado);
    }

    public List<Coordenada> getCoordenadasDisponiveisParaTiro() {
        List<Coordenada> disponiveis = new ArrayList<>();
        for (int linha = 0; linha < TAMANHO; linha++) {
            for (int coluna = 0; coluna < TAMANHO; coluna++) {
                Coordenada coordenada = new Coordenada(linha, coluna);
                StatusCelula status = getStatus(coordenada);
                if (status != StatusCelula.AGUA_ATINGIDA && status != StatusCelula.EMBARCACAO_ATINGIDA) {
                    disponiveis.add(coordenada);
                }
            }
        }
        return List.copyOf(disponiveis);
    }

    public void posicionarFrotaPadraoAleatoria(Random random) {
        Objects.requireNonNull(random, "random nao pode ser nulo");
        if (temFrota()) {
            throw new IllegalStateException("tabuleiro ja possui frota");
        }

        List<Embarcacao> frota = List.of(new PortaAvioes(), new NavioEscolta(), new Submarino(), new Caca());
        for (Embarcacao embarcacao : frota) {
            posicionarAleatoriamente(embarcacao, random);
        }
    }

    private void posicionarAleatoriamente(Embarcacao embarcacao, Random random) {
        for (int tentativas = 0; tentativas < 1000; tentativas++) {
            Coordenada inicio = new Coordenada(random.nextInt(TAMANHO), random.nextInt(TAMANHO));
            Orientacao orientacao = random.nextBoolean() ? Orientacao.HORIZONTAL : Orientacao.VERTICAL;
            if (podePosicionar(embarcacao, inicio, orientacao)) {
                posicionarEmbarcacao(embarcacao, inicio, orientacao);
                return;
            }
        }
        throw new IllegalStateException("nao foi possivel posicionar a frota");
    }

    private Celula celulaEm(Coordenada coordenada) {
        Objects.requireNonNull(coordenada, "coordenada nao pode ser nula");
        return grade[coordenada.linha()][coordenada.coluna()];
    }

    private List<Coordenada> calcularCoordenadas(
            Embarcacao embarcacao,
            Coordenada inicio,
            Orientacao orientacao) {
        List<Coordenada> coordenadas = new ArrayList<>();
        for (int deslocamento = 0; deslocamento < embarcacao.getTamanho(); deslocamento++) {
            int linha = inicio.linha() + (orientacao == Orientacao.VERTICAL ? deslocamento : 0);
            int coluna = inicio.coluna() + (orientacao == Orientacao.HORIZONTAL ? deslocamento : 0);
            if (!estaDentro(linha, coluna)) {
                return List.of();
            }
            coordenadas.add(new Coordenada(linha, coluna));
        }
        return coordenadas;
    }

    private List<Coordenada> coordenadasDoDisparo(Coordenada alvo, TipoDisparo tipo) {
        Set<Coordenada> coordenadas = new LinkedHashSet<>();
        adicionarSeDentro(coordenadas, alvo.linha(), alvo.coluna());
        if (tipo == TipoDisparo.CASCATA) {
            adicionarSeDentro(coordenadas, alvo.linha(), alvo.coluna() + 1);
        } else if (tipo == TipoDisparo.ESTRELA) {
            adicionarSeDentro(coordenadas, alvo.linha() - 1, alvo.coluna());
            adicionarSeDentro(coordenadas, alvo.linha() + 1, alvo.coluna());
            adicionarSeDentro(coordenadas, alvo.linha(), alvo.coluna() - 1);
            adicionarSeDentro(coordenadas, alvo.linha(), alvo.coluna() + 1);
        }
        return List.copyOf(coordenadas);
    }

    private void adicionarSeDentro(Set<Coordenada> coordenadas, int linha, int coluna) {
        if (estaDentro(linha, coluna)) {
            coordenadas.add(new Coordenada(linha, coluna));
        }
    }

    private boolean estaDentro(int linha, int coluna) {
        return linha >= 0 && linha < TAMANHO && coluna >= 0 && coluna < TAMANHO;
    }
}
