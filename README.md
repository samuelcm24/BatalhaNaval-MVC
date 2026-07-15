# Batalha Naval MVC

Versao aprimorada do jogo Batalha Naval em Java, com interface Swing, arquitetura MVC, testes automatizados e empacotamento Maven executavel.

Este projeto moderniza uma implementacao original de Batalha Naval criada em Java com interface grafica para estudo de Programacao Orientada a Objetos. O README original atribuia o projeto a Leonardo Guths e Leandro Tavares; essa informacao e preservada aqui como referencia historica do codigo-base.

## Objetivo Academico

O objetivo deste trabalho e demonstrar a evolucao de um sistema Java Swing legado para uma arquitetura mais organizada, testavel e executavel em Java 21, mantendo as funcionalidades do jogo original e aplicando melhorias propostas na documentacao do estudo de caso.

## Versao Original E Versao Aprimorada

Na versao original, boa parte das regras, estado do jogo, cronometro, ranking e interface estavam concentrados em classes Swing. Isso dificultava testes automatizados e manutencao.

Na versao aprimorada, o projeto foi reorganizado em MVC:

- Model: regras de partida, tabuleiro, celulas, embarcacoes, IA e ranking.
- Control: coordenacao da partida e persistencia do ranking.
- View: telas Swing responsaveis por renderizar estado e encaminhar acoes.

As classes legadas foram removidas na etapa final depois que a nova aplicacao passou a iniciar exclusivamente pela nova interface MVC.

## Funcionalidades

- Partida aleatoria com frota do jogador e do computador geradas automaticamente.
- Configuracao manual do tabuleiro do jogador.
- Posicionamento horizontal e vertical de Porta-avioes, Navio de Escolta, Submarino e Caca.
- Tabuleiro do jogador e do computador em interface Swing.
- Imagens originais das embarcacoes e dos impactos carregadas pelo classpath.
- Embarcacoes do computador ocultas ate serem atingidas.
- Dicas limitadas que indicam se existe embarcacao na linha ou coluna escolhida.
- Turnos alternados entre jogador e IA adversaria.
- Ranking persistente com Top 15 de vitorias.
- Marcador de tempo visual em formato `mm:ss`.
- Tela final com resultado, tempo, ranking, reinicio, novo jogo e saida.

## Disparos E Dicas

Os disparos preservam a proposta do jogo:

- Simples: atinge uma unica celula.
- Cascata: atinge a celula alvo e a celula imediatamente a direita.
- Estrela: atinge alvo, acima, abaixo, esquerda e direita.
- Porta-avioes: disparo especial carregado, disponivel apos carga suficiente e com Porta-avioes ativo.

Cada tipo de disparo depende de uma embarcacao ativa do jogador. As dicas nao consomem turno e sao limitadas a tres usos por partida.

## Ranking Persistente

O ranking fica salvo no diretorio do usuario em:

```text
%USERPROFILE%\.batalha-naval\ranking.tsv
```

no Windows, ou:

```text
~/.batalha-naval/ranking.tsv
```

no Linux.

O arquivo usa texto UTF-8, formato versionado, nomes codificados em Base64 para evitar conflito com separadores, e ordenacao por menor tempo.

## Arquitetura MVC

```text
src/main/java/batalhanaval
├── BatalhaNaval.java
├── control
│   ├── JogoController.java
│   └── RankingController.java
├── model
│   ├── Partida.java
│   ├── Tabuleiro.java
│   ├── Celula.java
│   ├── Embarcacao.java
│   ├── PortaAvioes.java
│   ├── NavioEscolta.java
│   ├── Submarino.java
│   ├── Caca.java
│   ├── IAAdversaria.java
│   ├── Ranking.java
│   ├── JogadorInfo.java
│   └── tipos auxiliares
└── view
    ├── StartJanela.java
    ├── SetupJanela.java
    ├── GameJanela.java
    ├── EndJanela.java
    ├── ComponenteCronometro.java
    └── RecursosImagens.java
```

Os recursos visuais ficam em:

```text
src/main/resources/batalhanaval/imagens
```

## Tecnologias

- Java 21
- Java Swing
- Maven Wrapper
- JUnit 5
- GitHub Actions

## Requisitos

- JDK 21 instalado.
- Terminal PowerShell no Windows ou shell no Linux.
- Nao e necessario Maven global; use o Maven Wrapper do projeto.

## Comandos

Windows:

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd clean package
java -jar target\batalha-naval-1.0.0.jar
```

Linux:

```bash
chmod +x ./mvnw
./mvnw clean test
./mvnw clean package
java -jar target/batalha-naval-1.0.0.jar
```

## Testes

O projeto possui 79 testes automatizados cobrindo o Model e o Control, incluindo:

- validacao de coordenadas;
- embarcacoes e dano;
- celulas e impactos;
- tabuleiro e padroes de disparo;
- IA adversaria;
- fluxo de partida;
- ranking persistente;
- metodos de leitura usados pela View.

Para executar:

```powershell
.\mvnw.cmd clean verify
```

## Documentacao

- [Relatorio e diagramas](documentacao)
- [Roteiro do video](documentacao/ROTEIRO_VIDEO.md)
- [Contribuicoes](CONTRIBUICOES.md)

## Desenvolvimento Assistido Por IA

Foi utilizado o Codex no VS Code como ferramenta agêntica de apoio ao desenvolvimento. A ferramenta analisou o codigo original, leu a documentacao, implementou blocos incrementais, auxiliou nas revisoes, executou validacoes e ajudou a manter a separacao entre Model, Control e View.

O uso do Codex corresponde a uma ferramenta agêntica equivalente ao fluxo proposto na atividade: analise do projeto, planejamento incremental, implementacao assistida, revisao e verificacao por comandos.
