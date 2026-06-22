public class Posicao
{
   
    int linha;
    int coluna;

    public Posicao(int linha, int coluna){
        this.linha = linha;
        this.coluna = coluna;
    }
    public int getLinha(){ return linha;}
    public int getColuna(){return coluna;}

    @Override
    public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Posicao)) return false;
    Posicao p = (Posicao) o;
    return linha == p.linha && coluna == p.coluna;
}

    @Override
    public int hashCode() {
    return 31 * linha + coluna;
}
}