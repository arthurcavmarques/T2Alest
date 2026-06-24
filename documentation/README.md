# Alest II — Cavalo em Tabuleiro Toroidal

**Dupla:** Arthur Marques e João Pedro Bianchi

Encontra o **menor número de movimentos** de um cavalo de xadrez entre uma posição inicial
`C` e um alvo `S`, em um tabuleiro com obstáculos (`x`) e bordas toroidais (que "se colam").
O problema é modelado como grafo e resolvido com **BFS (busca em largura)**.

## Documentos

- 📄 **[relatorio.md](relatorio.md)** — relatório do trabalho (problema, modelagem, algoritmo,
  resultados dos testes e conclusões).
- 🎤 **[roteiro-apresentacao.md](roteiro-apresentacao.md)** — roteiro de fala para a apresentação.
- 🗒️ **[plan.md](plan.md)** — planejamento e checklist do desenvolvimento.

## Estrutura do código

| Arquivo          | Responsabilidade |
|------------------|------------------|
| `Main.java`      | Lê o tabuleiro do `stdin`, localiza `C`/`S`, chama a BFS e imprime o resultado. |
| `Tabuleiro.java` | Grid, wrap toroidal (`wrapRow`/`wrapCol`), obstáculos e vizinhança do cavalo. |
| `BuscaBFS.java`  | BFS que calcula o número mínimo de movimentos. |
| `Posicao.java`   | Coordenada `(linha, coluna)`. |
| `tests/`         | Casos de teste (`caso0.txt` … `caso12.txt`). |

## Como compilar e executar

```bash
javac -d out *.java
java -cp out Main < tests/caso1.txt
```

No Windows (PowerShell), use `cmd` para redirecionar o `stdin`:

```powershell
cmd /c "java -cp out Main < tests\caso1.txt"
```

Saída esperada: um inteiro com o número mínimo de movimentos (ou `-1` se inalcançável).
Resultados completos dos casos estão no [relatorio.md](relatorio.md).
