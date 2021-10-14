public class BCP {
    private String nomeProcesso;
    private Registrador CP; // contador de programa
    private Registrador X;
    private Registrador Y;
    private EstadoProcesso estadoProcesso;
    private String[] codigo; // código do programa
    private int espera;
    private int contInterrompidos;

    public BCP(){
        this.CP=new Registrador();
        this.espera=0;
        this.contInterrompidos=0;
    }
    
    public void aumentaInterrompidos(){
        this.contInterrompidos++;
    }
    public boolean doisInterrompidos(){
        if(contInterrompidos>1){
            return true;
        }
        return false;
    }
    public void setEspera(int espera){
        this.espera=espera;
    }
    public int getEspera(){
        return espera;
    }
    public String getNomeProcesso() {
        return nomeProcesso;
    }

    public void setNomeProcesso(String nomeProcesso) {
        this.nomeProcesso = nomeProcesso;
    }

    public int getCP() {
        return CP.getValor();
    }

    public void setCP(Registrador cP) {
        CP = cP;
    }
    public void aumentaCP() {
        CP.setValor(CP.getValor()+1);;
    }
    public int getX() {
        return X.getValor();
    }

    public void setX(Registrador x) {
        X=x;
    }

    public int getY() {
        return Y.getValor();
    }

    public void setY(Registrador y) {
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
