
public class Main {
    
    private static Posicao start;
    private static Posicao target;

    
    public static void main(String[] args) throws Exception {
        Tabuleiro tab = lerTabuleiro();

        // Executa a BFS a partir da posição inicial 'C' até o alvo 'S'.
        // Retorna o menor número de movimentos do cavalo, ou -1 se inalcançável.
        int passos = new BuscaBFS().bfs(getStart(), getTarget(), tab);
        System.out.println(passos);
    }
    public static Tabuleiro lerTabuleiro() throws Exception {
    java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));

    // Ler todas as linhas do stdin (arquivo sem cabeçalho)
    java.util.List<String> lines = new java.util.ArrayList<>();
    String line;
    while ((line = br.readLine()) != null) {
        // remover espaços à direita (padding) para tolerar arquivos com trailing spaces
        line = line.replaceAll("\\s+$", "");
        if (line.length() == 0) continue; // pular linhas vazias eventuais
        lines.add(line);
    }

    if (lines.isEmpty()) throw new IllegalArgumentException("Entrada vazia ou sem linhas do tabuleiro");

    int R = lines.size();
    int C = lines.get(0).length();

    // validar consistência das linhas
    for (int r = 0; r < R; r++) {
        if (lines.get(r).length() != C) {
            throw new IllegalArgumentException("Linha " + r + " tem tamanho diferente do esperado: " + lines.get(r).length() + " vs " + C);
        }
    }

    Tabuleiro tab = Tabuleiro.fromLines(R, C, lines);

    for (int r = 0; r < R; r++) {
        for (int c = 0; c < C; c++) {
            char ch = tab.cellAt(r, c);
            if (ch == 'C') start = new Posicao(r, c);
            else if (ch == 'S') target = new Posicao(r, c);
        }
    }
    if (start == null) throw new IllegalStateException("Posicao inicial 'C' nao encontrada");
    if (target == null) throw new IllegalStateException("Posicao alvo 'S' nao encontrada");

    return tab;
}

public static Posicao getStart() { return start; }
public static Posicao getTarget() { return target; }

    
}
