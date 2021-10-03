public class BCP {
    private String nomeProcesso;
    private Registrador CP; // contador de programa
    private Registrador X;
    private Registrador Y;
    private EstadoProcesso estadoProcesso;
    private String[] codigo; // código do programa

    public String getNomeProcesso() {
        return nomeProcesso;
    }

    public void setNomeProcesso(String nomeProcesso) {
        this.nomeProcesso = nomeProcesso;
    }

    public Registrador getCP() {
        return CP;
    }

    public void setCP(Registrador cP) {
        CP = cP;
    }

    public Registrador getX() {
        return X;
    }

    public void setX(Registrador x) {
        X = x;
    }

    public Registrador getY() {
        return Y;
    }

    public void setY(Registrador y) {
        Y = y;
    }

    public EstadoProcesso getEstadoProcesso() {
        return estadoProcesso;
    }

    public void setEstadoProcesso(EstadoProcesso estadoProcesso) {
        this.estadoProcesso = estadoProcesso;
    }

    public String[] getCodigo() {
        return codigo;
    }

    public void setCodigo(String[] codigo) {
        this.codigo = codigo;
    }

    public enum EstadoProcesso {
        Executando(0), Pronto(1), Bloqueado(2);

        public int valorCarta;

        EstadoProcesso(int valor) {
            valorCarta = valor;
        }
    }
}
