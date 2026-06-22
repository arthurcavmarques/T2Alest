
Alest II - Trabalho: Cavalo em Tabuleiro Toroidal
Dupla: Arthur Marques e Joao Pedro Bianchi
Disciplina: Alest II

1. Qual o problema sendo resolvido
- Encontrar o menor número de movimentos que um cavalo (movimento do xadrez) precisa
	para ir da posição inicial `C` até a posição alvo `S` em um tabuleiro com obstáculos `x`.
	O tabuleiro é toroidal: sair por uma borda entra-se na borda oposta.

2. Como o problema foi modelado
- Cada célula não-obstáculo é um vértice do grafo implícito.
- Arestas ligam células que o cavalo alcança em um lance (8 offsets: ±2,±1 e ±1,±2),
	aplicando wrap toroidal nas coordenadas.

3. Como é o processo de solução (algoritmos e exemplos)
- Usamos BFS (busca em largura) para encontrar o menor número de movimentos em um grafo não ponderado.
- Implementação principal:
	- `Tabuleiro` mantém `char[][] grid`, utilitários de wrap, `isObstacle` e `getNeighbors`.
	- `Posicao` representa coordenadas `(linha,coluna)`.
	- `BuscaBFS` executa BFS com `int[][] dist` e `ArrayDeque<Posicao>`.

4. Resultados dos casos de teste
- A completar pela Pessoa 2: executar `tests/*` e colar saídas esperadas e obtidas.

5. Conclusões
- A completar pela Pessoa 2.

