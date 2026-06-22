    public class BuscaBFS {
        // Classe responsável por executar a busca em largura (BFS)
        // Implementação usa uma fila (`ArrayDeque`) e uma matriz `dist` com -1 indicando não visitado.

        // Retorna o número mínimo de movimentos do cavalo de `start` até `target`, ou -1 se inalcançável.
        public int bfs(Posicao start, Posicao target, Tabuleiro tab) {
        int R = tab.getRows();
        int C = tab.getCols();
            // Caso trivial: origem == destino
            if (start.equals(target)) return 0;

        int[][] dist = new int[R][C];
        for (int i = 0; i < R; i++) java.util.Arrays.fill(dist[i], -1);

            java.util.ArrayDeque<Posicao> fila = new java.util.ArrayDeque<>();
                // Marca distância da posição inicial como 0 e a enfileira
                dist[start.getLinha()][start.getColuna()] = 0;
                fila.addLast(start);

        while (!fila.isEmpty()) {
        Posicao cur = fila.removeFirst();
                // `cur` é a posição sendo expandida (retirada da fila)
        int cr = cur.getLinha();
        int cc = cur.getColuna();
        int d = dist[cr][cc];

                // Gera vizinhos válidos usando `Tabuleiro.getNeighbors`,
                // que já aplica wrap toroidal e filtra obstáculos.
                for (Posicao nb : tab.getNeighbors(cur)) {
            int nr = nb.getLinha();
            int nc = nb.getColuna();
            if (dist[nr][nc] == -1) {
                        // Visita pela primeira vez: registra distância e enfileira
                        dist[nr][nc] = d + 1;
                        // Se for o alvo, retorna imediatamente (garante mínimo)
                        if (nb.equals(target)) return dist[nr][nc];
                        fila.addLast(nb);
            }
        } 
        
    }
    return -1; // target inalcançável
    }

}
