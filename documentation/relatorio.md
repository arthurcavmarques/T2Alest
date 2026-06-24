# Relatório — Cavalo em Tabuleiro Toroidal

**Disciplina:** Alest II
**Dupla:** Arthur Marques e João Pedro Bianchi

> Este relatório segue a estrutura solicitada no enunciado:
> (1) qual o problema, (2) como foi modelado, (3) processo de solução com exemplos e
> algoritmos, (4) resultados dos casos de teste e (5) conclusões.

---

## 1. Qual o problema sendo resolvido

O objetivo é encontrar o **menor número de movimentos** que um cavalo de xadrez precisa
para sair de uma posição inicial `C` e chegar a uma posição alvo `S` em um tabuleiro com
obstáculos.

Características do problema:

- O cavalo se move em "L", com os **8 movimentos** clássicos do xadrez.
- Há **obstáculos** (`x`): casas onde o cavalo não pode parar.
- O tabuleiro é **toroidal**: ao sair por uma borda, o cavalo reaparece na borda oposta
  (a última coluna é vizinha da primeira; a última linha é vizinha da primeira — como
  no cenário do Pac-Man, ou na superfície de um *torus*/rosquinha).

**Entrada** (lida da entrada padrão, sem cabeçalho):

| Símbolo | Significado        |
|:-------:|--------------------|
| `.`     | casa livre         |
| `x`     | obstáculo          |
| `C`     | posição inicial    |
| `S`     | posição alvo       |

**Saída:** um único inteiro — o número mínimo de movimentos, ou `-1` se o alvo for
inalcançável.

---

## 2. Como o problema foi modelado

O problema foi modelado como uma **busca de caminho mínimo em um grafo não ponderado**:

- Cada casa **não-obstáculo** é um **vértice**.
- Existe uma **aresta** entre duas casas quando o cavalo consegue ir de uma para a outra
  em **um único lance**. Os 8 movimentos correspondem aos offsets `(±2,±1)` e `(±1,±2)`:

  ```
  DR = {-2,-2,-1,-1, 1, 1, 2, 2}
  DC = {-1, 1,-2, 2,-2, 2,-1, 1}
  ```

- O comportamento **toroidal** é aplicado nas coordenadas com **aritmética modular**:

  ```
  nr = ((r + DR[k]) mod R + R) mod R
  nc = ((c + DC[k]) mod C + C) mod C
  ```

  (o `+R`/`+C` extra corrige o fato de que, em Java, o `%` de um número negativo continua
  negativo — assim a coordenada volta para a faixa válida).

- Movimentos que caem em obstáculos (`x`) são descartados.

Como **todas as arestas têm o mesmo peso** (1 movimento), o caminho com menos arestas é
exatamente o menor número de lances — cenário ideal para **BFS (busca em largura)**.

### Organização das classes

| Classe        | Responsabilidade |
|---------------|------------------|
| `Posicao`     | Representa uma casa `(linha, coluna)`; implementa `equals`/`hashCode`. |
| `Tabuleiro`   | Mantém o `char[][] grid` e os utilitários: `cellAt`, `isObstacle`, `wrapRow`, `wrapCol`, `getNeighbors` e o factory `fromLines`. |
| `BuscaBFS`    | Executa a BFS e devolve a distância mínima. |
| `Main`        | Lê o tabuleiro, localiza `C` e `S`, chama a BFS e imprime o resultado. |

---

## 3. Como é o processo de solução (algoritmos e exemplos)

### 3.1 Algoritmo — BFS

A BFS explora o grafo **em camadas (níveis)** a partir de `C`: primeiro todas as casas a
1 movimento, depois as a 2, depois as a 3, e assim por diante (analogia da pedra na água:
as ondas se espalham em anéis crescentes). Por isso, **a primeira vez que a busca alcança
`S`, a distância registrada é necessariamente a mínima**.

Estruturas utilizadas:
- uma **fila** (`ArrayDeque<Posicao>`) com os vértices a expandir (FIFO → garante a ordem
  por camadas);
- uma matriz `int[][] dist` inicializada com `-1` (não visitado), que serve **ao mesmo
  tempo** como marcador de "visitado" e como armazenamento da distância. Com isso, cada
  casa entra na fila uma única vez.

### 3.2 Pseudocódigo

```
BFS(start, target, tabuleiro):
    se start == target: retorne 0

    dist[][] <- -1 para todas as casas
    dist[start] <- 0
    fila <- [start]

    enquanto fila não vazia:
        cur <- fila.removeFirst()
        d   <- dist[cur]
        para cada vizinho nb de cur (com wrap toroidal, sem obstáculos):
            se dist[nb] == -1:            # ainda não visitado
                dist[nb] <- d + 1         # mais um movimento
                se nb == target: retorne dist[nb]
                fila.addLast(nb)

    retorne -1                            # target inalcançável
```

### 3.3 O wrap toroidal (aritmética modular)

Quando um pulo cai fora do tabuleiro (ex.: coluna `40` em um tabuleiro de 40 colunas, cujos
índices válidos vão de `0` a `39`), o operador módulo "dá a volta":

| Coluna calculada | `% 40` | Resultado | Efeito |
|:----------------:|:------:|:---------:|--------|
| `41`             | `41 % 40` | `1`    | saiu pela direita → voltou na coluna 1 |
| `40`             | `40 % 40` | `0`    | saiu pela direita → voltou na coluna 0 |
| `-1`             | ajuste  | `39`     | saiu pela esquerda → voltou na coluna 39 |

O ajuste `w < 0 ? w + cols : w` é necessário porque, em Java, `-1 % 40` resulta `-1` (e não
`39`). Somar o tamanho corrige isso e completa o efeito toroidal.

### 3.4 Exemplo passo a passo (`caso1`, tabuleiro 10×10)

`C` em `(7,7)` e `S` em `(6,8)`:

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
- **Nível 1:** a partir de `(7,7)` o cavalo alcança 8 candidatos; após filtrar obstáculos e
  aplicar o wrap, nenhum é `(6,8)` ainda.
- **Nível 2:** expandindo as casas do nível 1, o cavalo chega em `(6,8) = S`.

**Resultado: 2 movimentos** — confere com a saída do programa.

---

## 4. Resultados dos casos de teste

Ambiente: JDK do IntelliJ (`jbr`); execução via `java -cp out Main < tests/casoN.txt`.

| Caso   | Dimensão (L×C) | Saída (movimentos) | Observação |
|--------|----------------|:------------------:|------------|
| caso0  | 20×41\*        | —                  | **Arquivo malformado:** a linha 0 tem 41 caracteres e as demais 40. O programa rejeita corretamente com `IllegalArgumentException`. |
| caso1  | 10×10          | **2**              | OK |
| caso2  | 20×20          | **6**              | OK |
| caso3  | 30×30          | **7**              | OK |
| caso4  | 40×60          | **15**             | OK |
| caso5  | 50×70          | **10**             | OK |
| caso6  | 60×80          | **16**             | OK |
| caso7  | 70×90          | **19**             | OK |
| caso8  | 80×80          | **12**             | OK |
| caso9  | 90×90          | **24**             | OK |
| caso10 | 100×100        | **27**             | OK |
| caso11 | 200×200        | **50**             | OK |
| caso12 | 400×400        | **85**             | OK |

\* No `caso0` a primeira linha contém um caractere a mais que as demais, tornando o grid
não-retangular. Como o tabuleiro toroidal exige linhas de tamanho uniforme, a validação de
entrada em `Main.lerTabuleiro()` aborta com mensagem explicando a inconsistência. Os demais
**12 casos rodam corretamente**, do menor (10×10) ao maior (400×400).

### Análise de complexidade e eficiência

Sendo `R` linhas, `C` colunas e `V = R·C` casas:

- **Tempo:** cada casa é enfileirada/desenfileirada no máximo uma vez, e gera um número
  constante de vizinhos (8) → **O(R·C)**.
- **Espaço:** a matriz `dist` e a fila são **O(R·C)**.
- **Otimalidade:** por ser grafo não ponderado, a BFS garante o caminho mínimo.

A BFS é preferível a Dijkstra/A\* aqui justamente porque todas as arestas têm peso 1.

### Como compilar e executar

```bash
javac -d out *.java
java -cp out Main < tests/caso1.txt
```

No Windows (PowerShell), use `cmd` para o redirecionamento de `stdin`:

```powershell
cmd /c "java -cp out Main < tests\caso1.txt"
```

---

## 5. Conclusões

- A modelagem como **grafo + BFS** resolveu o problema de forma **simples, ótima e
  eficiente** (`O(R·C)`), inclusive nos casos maiores (400×400).
- Isolar o **wrap toroidal** no `Tabuleiro` manteve a BFS **genérica** — ela não precisa
  saber que o tabuleiro é circular.
- Reaproveitar a matriz `dist` como marcador de visita **simplificou o código** e dispensou
  uma estrutura `visited` separada.
- A **validação de entrada** se mostrou útil ao detectar o `caso0` malformado em vez de
  produzir um resultado silenciosamente incorreto.
- Os resultados nos 12 casos válidos foram consistentes com o comportamento esperado do
  cavalo em um tabuleiro toroidal.
