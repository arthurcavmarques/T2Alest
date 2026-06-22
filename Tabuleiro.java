public class Tabuleiro {
    private final int rows;
    private final int cols;
    private final char[][]grid;


    public Tabuleiro(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        this.grid = new char[rows][cols];
}
    // Factory a partir de linhas de texto (cada linha tem cols caracteres)
    public static Tabuleiro fromLines(int rows, int cols, java.util.List<String> lines) {
    Tabuleiro t = new Tabuleiro(rows, cols);
    for (int r = 0; r < rows; r++) {
        String line = lines.get(r);
        for (int c = 0; c < cols; c++) {
            t.grid[r][c] = line.charAt(c);
        }
    }
    return t;
}
    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public char[][] getGrid() { return grid; }

    public char cellAt(int r, int c) {
    return grid[r][c];
}

public boolean isObstacle(int r, int c) {
    return grid[r][c] == 'x';
}

// retorna coordenadas aplicando wrap (garante 0..rows-1 e 0..cols-1)
public int wrapRow(int r) {
    int w = (r % rows);
    return w < 0 ? w + rows : w;
}
public int wrapCol(int c) {
    int w = (c % cols);
    return w < 0 ? w + cols : w;
}
    public java.util.List<Posicao> getNeighbors(Posicao p) {
    java.util.List<Posicao> neighbors = new java.util.ArrayList<>();
 int r = p.getLinha();
 int c = p.getColuna();
 int[] DR = {-2,-2,-1,-1,1,1,2,2};
 int[] DC = {-1,1,-2,2,-2,2,-1,1};
   for (int k = 0; k < 8; k++) {
 int nr = wrapRow(r + DR[k]);
 int nc = wrapCol(c + DC[k]);
    if (!isObstacle(nr, nc)) {
 neighbors.add(new Posicao(nr, nc));
}
}
return neighbors;
}
}
