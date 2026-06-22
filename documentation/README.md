# Alest II — Trabalho: Cavalo em Tabuleiro Toroidal

**Dupla:** Arthur Marques e João Pedro Bianchi
**Disciplina:** Alest II

---

## 1. Qual o problema sendo resolvido

Encontrar o **menor número de movimentos** que um cavalo (peça de xadrez) precisa para
ir da posição inicial `C` até a posição alvo `S` em um tabuleiro com obstáculos `x`.

O tabuleiro é **toroidal**: ao sair por uma borda, o cavalo reaparece na borda oposta
(a última coluna é vizinha da primeira, e a última linha é vizinha da primeira).

**Entrada:** um tabuleiro de texto (sem cabeçalho), lido pela entrada padrão (`stdin`), onde:
- `.` = célula livre
- `x` = obstáculo (não pode ser ocupado)
- `C` = posição inicial do cavalo
- `S` = posição alvo

**Saída:** um único inteiro — o número mínimo de movimentos, ou `-1` se o alvo for inalcançável.

---

## 2. Como o problema foi modelado

O problema é modelado como uma **busca de caminho mínimo em um grafo não ponderado**:

- Cada célula **não-obstáculo** é um **vértice** do grafo.
- Existe uma **aresta** entre duas células se o cavalo consegue ir de uma para a outra
  em **um único lance**. O cavalo tem 8 movimentos possíveis (offsets `(±2,±1)` e `(±1,±2)`):

  ```
  DR = {-2,-2,-1,-1, 1, 1, 2, 2}
  DC = {-1, 1,-2, 2,-2, 2,-1, 1}
  ```

- Como o tabuleiro é toroidal, ao calcular a célula de destino aplicamos **wrap** nas
  coordenadas com aritmética modular:

  ```
  nr = ((r + DR[k]) mod R + R) mod R
  nc = ((c + DC[k]) mod C + C) mod C
  ```

  (o `+R`/`+C` extra garante resultado positivo mesmo para índices negativos em Java).

- Movimentos que caem em obstáculos (`x`) são descartados.

Como **todas as arestas têm o mesmo peso** (1 movimento), o caminho mínimo em número de
arestas é exatamente o menor número de lances — o cenário ideal para **BFS (busca em largura)**.

### Organização das classes

| Classe        | Responsabilidade |
|---------------|------------------|
| `Posicao`     | Representa uma célula `(linha, coluna)`; implementa `equals`/`hashCode`. |
| `Tabuleiro`   | Mantém o `char[][] grid` e os utilitários: `cellAt`, `isObstacle`, `wrapRow`, `wrapCol`, `getNeighbors` e o factory `fromLines`. |
| `BuscaBFS`    | Executa a BFS sobre o tabuleiro retornando a distância mínima. |
| `Main`        | Lê o tabuleiro do `stdin`, localiza `C` e `S`, chama a BFS e imprime o resultado. |

---

## 3. Como é o processo de solução (algoritmos e exemplos)

### 3.1 Algoritmo — BFS

A BFS explora o grafo em **camadas (níveis)** a partir de `C`. Todas as células
alcançáveis em 1 movimento são visitadas antes das alcançáveis em 2, e assim por diante.
A primeira vez que `S` é alcançado, a distância registrada é necessariamente a mínima.

Usamos:
- uma **fila** (`ArrayDeque<Posicao>`) com os vértices a expandir;
- uma matriz `int[][] dist` inicializada com `-1` (não visitado), que serve **ao mesmo
  tempo** como marcador de "visitado" e como armazenamento da distância.

### 3.2 Pseudocódigo

```
BFS(start, target, tabuleiro):
    se start == target: retorne 0

    dist[][] <- -1 para todas as células
    dist[start] <- 0
    fila <- [start]

    enquanto fila não vazia:
        cur  <- fila.removeFirst()
        d    <- dist[cur]
        para cada vizinho nb de cur (com wrap toroidal, sem obstáculos):
            se dist[nb] == -1:           # ainda não visitado
                dist[nb] <- d + 1
                se nb == target: retorne dist[nb]
                fila.addLast(nb)

    retorne -1                            # target inalcançável
```

### 3.3 Exemplo passo a passo (`caso1.txt`)

Tabuleiro 10×10. Posição inicial `C` em `(7,7)` e alvo `S` em `(6,8)`:

```
x.........
x.........
....x.....
...x...x.x
..x..x..xx
...x......
....x.x.Sx
....x..C..
...x......
x...xxx...
```

- **Nível 0:** `{(7,7)}` — posição de `C`.
- **Nível 1:** a partir de `(7,7)` o cavalo alcança 8 candidatos; após filtrar obstáculos
  e aplicar wrap, nenhum deles é `(6,8)` ainda.
- **Nível 2:** expandindo as células do nível 1, o cavalo chega em `(6,8) = S`.

Resultado: **2 movimentos** — confere com a saída do programa.

---

## 4. Análise de complexidade e eficiência

Seja `R` o número de linhas e `C` o número de colunas, com `V = R·C` células.

- **Tempo:** cada célula é enfileirada/desenfileirada no máximo uma vez (graças ao
  marcador `dist == -1`). Para cada célula expandida, geramos um número **constante** de
  vizinhos (8). Logo, o custo total é **O(V) = O(R·C)**.
- **Espaço:** a matriz `dist` ocupa **O(R·C)** e a fila contém no máximo `O(R·C)` posições.
- **Otimalidade:** como o grafo é não ponderado, a BFS garante o caminho mínimo em número
  de movimentos.

A escolha da BFS (em vez de Dijkstra/A\*) é adequada justamente porque todas as arestas
têm peso 1; Dijkstra seria desnecessariamente mais custoso e A\* exigiria uma heurística
não trivial por causa do comportamento toroidal.

---

## 5. Resultados dos casos de teste

Ambiente: JDK do IntelliJ (`jbr`), execução via `java -cp out Main < tests/casoN.txt`.

| Caso       | Dimensão (L×C) | Saída (movimentos) | Observação |
|------------|----------------|--------------------|------------|
| caso0      | 20×41*         | —                  | **Arquivo malformado:** a linha 0 tem 41 caracteres e as demais 40. O programa rejeita corretamente com `IllegalArgumentException`. |
| caso1      | 10×10          | **2**              | OK |
| caso2      | 20×20          | **6**              | OK |
| caso3      | 30×30          | **7**              | OK |
| caso4      | 40×60          | **15**             | OK |
| caso5      | 50×70          | **10**             | OK |
| caso6      | 60×80          | **16**             | OK |
| caso7      | 70×90          | **19**             | OK |
| caso8      | 80×80          | **12**             | OK |
| caso9      | 90×90          | **24**             | OK |
| caso10     | 100×100        | **27**             | OK |
| caso11     | 200×200        | **50**             | OK |
| caso12     | 400×400        | **85**             | OK |

\* No `caso0` a primeira linha contém um caractere a mais que as demais, tornando o grid
não-retangular. Como o tabuleiro toroidal exige linhas de tamanho uniforme, a validação de
entrada em `Main.lerTabuleiro()` aborta com mensagem explicando a inconsistência. Os demais
12 casos rodam corretamente.

### Como compilar e executar

```bash
# Compilar (gera os .class na pasta out/)
javac -d out *.java

# Executar um caso
java -cp out Main < tests/caso1.txt
```

No Windows (PowerShell), o redirecionamento de `stdin` deve ser feito via `cmd`, pois o
operador `<` não é suportado diretamente:

```powershell
cmd /c "java -cp out Main < tests\caso1.txt"
```

---

## 6. Conclusões

- A modelagem do tabuleiro como grafo implícito, somada à BFS, resolve o problema de forma
  **simples, ótima e eficiente** (`O(R·C)`), inclusive nos casos maiores (400×400).
- O **wrap toroidal** foi tratado de forma isolada em `Tabuleiro.wrapRow`/`wrapCol`,
  mantendo a BFS genérica e independente da topologia do tabuleiro.
- Reutilizar a matriz `dist` como marcador de visita simplificou o código e eliminou a
  necessidade de uma estrutura `visited` separada.
- A validação de entrada se mostrou útil: ela detectou o `caso0.txt` malformado em vez de
  produzir um resultado silenciosamente incorreto.
- Os resultados obtidos nos 12 casos válidos foram consistentes com o comportamento
  esperado do cavalo em um tabuleiro toroidal.
