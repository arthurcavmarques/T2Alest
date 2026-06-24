# Roteiro de Apresentação — Cavalo em Tabuleiro Toroidal

**Dupla:** Arthur Marques e João Pedro Bianchi · **Disciplina:** Alest II
**Duração alvo:** ~8 a 10 minutos

> Estrutura segue exatamente o que o enunciado pede:
> (1) o problema · (2) modelagem · (3) processo de solução com exemplos e algoritmos · (4) resultados dos testes · (5) conclusões.

---

## 0. Abertura (~30s) — *[Arthur]*

> "Bom dia/boa tarde. Nós somos o Arthur e o João, e nosso trabalho resolve o problema
> do **cavalo em um tabuleiro toroidal**. A ideia é descobrir o menor número de movimentos
> que um cavalo de xadrez precisa para chegar de um ponto a outro, em um tabuleiro com
> obstáculos e com as bordas 'coladas'. Vamos passar por: o problema, como modelamos,
> o algoritmo de solução, os resultados dos testes e as conclusões."

**Na tela:** título do trabalho + o tabuleiro de exemplo (`caso1`).

---

## 1. Qual o problema sendo resolvido (~1min) — *[Arthur]*

Pontos a falar:
- Encontrar o **menor número de movimentos** de um cavalo, da posição inicial `C` até o alvo `S`.
- O cavalo se move em "L" (8 movimentos do xadrez).
- O tabuleiro tem **obstáculos** (`x`) — casas proibidas.
- O tabuleiro é **toroidal**: quem sai por uma borda reaparece na borda oposta
  (analogia: Pac-Man / superfície de uma rosquinha).
- Entrada: tabuleiro de texto (`.` livre, `x` obstáculo, `C` início, `S` alvo).
  Saída: um número — o mínimo de movimentos, ou `-1` se for impossível.

**Na tela:** o tabuleiro `caso1` destacando o `C`, o `S` e os `x`.

---

## 2. Como o problema foi modelado (~1min30) — *[João]*

> "A chave do trabalho foi enxergar o tabuleiro como um **grafo**."

- Cada casa **livre** vira um **vértice**.
- Cada **movimento possível** do cavalo entre duas casas vira uma **aresta**.
- Como **todo movimento custa igual** (1 lance), o problema vira "achar o caminho com menos
  arestas" — ou seja, **caminho mínimo em grafo não ponderado**.
- Os 8 movimentos do cavalo são os offsets `(±2,±1)` e `(±1,±2)`.
- O comportamento toroidal é tratado com **aritmética modular** nas coordenadas (detalho no algoritmo).

**Na tela:** desenho de uma casa com as 8 setas dos movimentos do cavalo saindo dela.

---

## 3. Processo de solução: algoritmo e exemplos (~3min) — *[João]*

### 3.1 O algoritmo: BFS (Busca em Largura)
> "Para achar o caminho mínimo usamos **BFS**."

- A BFS explora o grafo **em camadas**: primeiro tudo a 1 movimento, depois a 2, depois a 3...
- **Analogia da pedra na água:** a 'onda' sai do `C` e se espalha de casa em casa.
- Por isso, **a primeira vez que a onda toca o `S`, é garantidamente o caminho mais curto.**

**Estruturas (mostrar `BuscaBFS.java`):**
- uma **fila** (`ArrayDeque`) com as casas a visitar (FIFO → garante a ordem por camadas);
- uma matriz `dist[][]` começando em `-1` (= não visitado), que guarda quantos movimentos
  levaram até cada casa. Cada vizinho recebe `dist do atual + 1`.
- Truque: `dist` serve **ao mesmo tempo** como "já visitei?" e como distância → cada casa
  entra na fila uma única vez.

### 3.2 O wrap toroidal (mostrar `Tabuleiro.wrapCol`)
> "Para 'colar' as bordas, usamos o resto da divisão (módulo)."

- Quando o pulo cai fora do tabuleiro (ex.: coluna `40` num tabuleiro de 40), o `% 40`
  traz de volta para a casa `0` — a borda 'dá a volta'.
- Cuidado em Java: `%` de número negativo continua negativo, então somamos o tamanho
  (`w < 0 ? w + cols : w`) para `-1` virar `39`.

### 3.3 Exemplo passo a passo (`caso1`, tabuleiro 10×10)
- `C` em `(7,7)`, `S` em `(6,8)`.
- **Nível 0:** só o `C`.
- **Nível 1:** os pulos válidos do `C` — nenhum é o `S` ainda.
- **Nível 2:** expandindo o nível 1, o cavalo chega no `S`.
- **Resposta: 2 movimentos** ✅

**Na tela:** o `caso1` com as camadas 0 → 1 → 2 coloridas até chegar no `S`.

---

## 4. Resultados dos casos de teste (~1min30) — *[Arthur]*

> "Rodamos os 13 casos disponibilizados pelos colegas."

| Caso | Dimensão | Movimentos | | Caso | Dimensão | Movimentos |
|---|---|---|---|---|---|---|
| caso1 | 10×10 | 2 | | caso7 | 70×90 | 19 |
| caso2 | 20×20 | 6 | | caso8 | 80×80 | 12 |
| caso3 | 30×30 | 7 | | caso9 | 90×90 | 24 |
| caso4 | 40×60 | 15 | | caso10 | 100×100 | 27 |
| caso5 | 50×70 | 10 | | caso11 | 200×200 | 50 |
| caso6 | 60×80 | 16 | | caso12 | 400×400 | 85 |

- **12 casos** rodaram corretamente, do menor (10×10) ao maior (400×400).
- O **`caso0`** tinha um **defeito no próprio arquivo** (uma linha com tamanho diferente
  das outras). Nosso programa **detecta e avisa** a inconsistência em vez de dar resposta
  errada — o que mostra que a validação de entrada funciona.
- **Eficiência:** o algoritmo é **O(R×C)** (linear no número de casas); por isso roda o
  tabuleiro de 400×400 (160 mil casas) sem dificuldade.

**Na tela:** a tabela de resultados + o terminal rodando um caso ao vivo (opcional).

---

## 5. Conclusões (~1min) — *[João]*

- Modelar o tabuleiro como **grafo + BFS** resolveu o problema de forma **simples, ótima e
  eficiente** (`O(R×C)`).
- Isolar o **wrap toroidal** no `Tabuleiro` manteve a BFS **genérica** — ela nem precisa
  saber que o tabuleiro é circular.
- Reaproveitar a matriz `dist` como marcador de visita **simplificou o código**.
- A **validação de entrada** se provou útil ao pegar o `caso0` malformado.
- Resultado: solução correta e testada nos 12 casos válidos, publicada no GitHub.

> "Era isso, obrigado! Alguma pergunta?"

---

## Apêndice — Possíveis perguntas do professor

- **Por que BFS e não Dijkstra/A\*?** Porque todas as arestas têm peso 1 (1 movimento);
  num grafo não ponderado a BFS já dá o ótimo e é mais barata. Dijkstra seria desperdício
  e A\* exigiria uma heurística difícil por causa do toro.
- **Qual a complexidade?** Tempo e espaço **O(R×C)** — cada casa é enfileirada no máximo
  uma vez e gera 8 vizinhos (constante).
- **Como garante o mínimo?** A BFS visita por camadas de distância crescente; a primeira
  vez que alcança o alvo é necessariamente o caminho mais curto.
- **E se o alvo for inalcançável?** A fila esvazia sem achar o `S` e retornamos `-1`.
- **Por que o `w < 0 ? w + cols`?** Em Java o `%` de negativo é negativo; o ajuste traz a
  coordenada de volta para a faixa válida `0..cols-1`, completando o efeito toroidal.
