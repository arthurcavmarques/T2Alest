## Plan: Modelagem inicial — cavalo toroidal (Q&A)

TL;DR: Mapear cada célula não-obstáculo para um vértice; arestas = movimentos do cavalo com wrap toroidal; usar BFS para menor número de movimentos. Responder perguntas 1–6 e aguardar confirmação antes de implementar.

**Steps**
1. Responder perguntas conceituais (feito).
2. Corrigir/implementar `Posicao` (representação do vértice) — *depende de aprovação*.
3. Implementar leitura do tabuleiro em `Main` e povoar `char[][] grid` — *depende de input format*.
4. Implementar método de vizinhança com wrap e checagem de obstáculos (p.ex. em `Tabuleiro` ou `Main`).
5. Implementar BFS (p.ex. `KnightSolver` ou método em `Main`) usando `ArrayDeque` + `boolean[][] visited`/`int[][] dist`.

**Relevant files**
- Main.java — ponto de entrada, leitura, coordenação.
- Posicao.java — representação do vértice (linha, coluna).
- (sugerido) Tabuleiro.java — utilitários: wrap, isObstacle, getNeighbors.
- BuscaBFS — algoritmo BFS, interface: run(start, target, tabuleiro) -> distância.

**Respostas (1–6)**
**Checklist do projeto**

- [x] `Posicao` — classe que representa uma célula (linha, coluna).
- [x] `Tabuleiro` — leitura e representação do grid, métodos `cellAt`, `isObstacle`, `wrapRow`, `wrapCol`, `fromLines`.
- [x] `Main.lerTabuleiro()` — leitura de arquivo sem cabeçalho, inferência de dimensões, localização de `C` e `S`.
- [x] `Tabuleiro.getNeighbors(Posicao)` — geração de vizinhança do cavalo com wrap toroidal e filtragem de obstáculos.
- [x] `BuscaBFS.bfs(...)` — implementação da BFS com `int[][] dist` e `ArrayDeque`, comentário nas linhas-chave.
- [ ] Integração e testes finais na `Main` (responsabilidade da Pessoa 2).
- [ ] Completar `README.md` com relatório final e resultados dos testes (responsabilidade da Pessoa 2).
 - [ ] Integração e testes finais na `Main` (responsabilidade da Pessoa 2).
 - [ ] Confirmar formato/nome do arquivo de caso de teste e como será passado ao programa (stdin vs args).
 - [ ] Completar `README.md` com relatório final e resultados dos testes (responsabilidade da Pessoa 2).
 - [ ] Adicionar pseudocódigo e exemplo passo-a-passo do algoritmo no README.
 - [ ] Incluir análise de complexidade e eficiência no README.
 - [ ] Incluir figuras/tabelas ilustrativas (diagrama do tabuleiro/grafo, exemplo de camadas BFS) no relatório.

**Instruções para Joao (passos para finalizar o trabalho)**

1. Integrar `BuscaBFS.bfs` no fluxo principal em `Main` (descomentar/usar a chamada já indicada):
   - Chamar `int passos = new BuscaBFS().bfs(getStart(), getTarget(), tab);`
   - Imprimir o resultado conforme o enunciado (ou `-1` se inalcançável).

2. Remover as linhas de print temporárias que imprimem o tabuleiro (para saída limpa).

3. Executar todos os casos de teste fornecidos (`tests/`) e registrar saídas esperadas/obtidas.

4. Atualizar `README.md` com: descrição do problema, modelagem, processo de solução (algoritmo BFS e comportamento toroidal), exemplos, resultados dos testes e conclusões.

5. (Opcional) Otimizações e verificações adicionais:
   - Substituir `Posicao` por representação `int id = r*C + c` se desejar performance e usar `int[] dist` em vez de `int[][]`.
   - Adicionar tratamento de entradas inválidas/erros com mensagens mais amigáveis.

6. Submeter commit final e preparar um pequeno script/instrução para executar os testes (p.ex. `run_tests.sh` ou `run_tests.bat`).

Se precisar, eu posso gerar o trecho de código para a integração na `Main` ou criar um script de execução dos testes. 