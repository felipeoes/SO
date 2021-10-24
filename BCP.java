public class BCP {

    private String nomeProcesso;
    private int CP; // contador de programa
    private int X;
    private int Y;
    private EstadoProcesso estadoProcesso;
    private String[] codigo; // código do programa
    private int espera;

    public BCP() {
        this.CP = new Registrador();
        this.espera = 0;
    }

    public void setEspera(int espera) {
        this.espera = espera;
    }

    public int getEspera() {
        return espera;
    }

    public String getNomeProcesso() {
        return nomeProcesso;
    }

    public void setNomeProcesso(String nomeProcesso) {
        this.nomeProcesso = nomeProcesso;
    }

   public int getCP() {
        return CP;
    }

    public void setCP(int cP) {
        CP = cP;
    }
    public void aumentaCP() {
        CP++;
    }
    public int getX() {
        return X;
    }

    public void setX(int x) {
        X=x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y=y;
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
