package javaapplication3;

public class Registrador {
    private boolean emUso;
    private int valor=0;
    
    public boolean isEmUso() {
        return emUso;
    }

    public void setEmUso(boolean emUso) {
        this.emUso = emUso;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int Valor) {
        this.valor = Valor;
    }
}
