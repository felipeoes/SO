import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Escalonador {
    private final static int TAM_MAX_PROGRAMA = 21;
    private int quantum;

    private static TabelaDeProcessos tabProcessos;
    private static List<Processo> processosProntos;
    private static List<Processo> processosBloq;
    private static Processo processoExec;
    private int interrompido;
    public int getQuantum() {
        return quantum;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }
    public boolean temProcessosProntos(){
        if(processosProntos.size()>0){
            return true;
        }
        return false;
    }
    public boolean temProcessosBloq(){
        if(processosBloq.size()>0){
            return true;
        }
        return false;
    }

    public Escalonador() {
        tabProcessos = new TabelaDeProcessos();
        processosProntos = new ArrayList<Processo>();
        processosBloq = new ArrayList<Processo>();
    }

    private static int carregaProcessosRAM() throws IOException {
        String caminhoBase = "./programas/";
        File dir = new File(caminhoBase); // diretorio alvo
        File[] arquivos = dir.listFiles();
        int quantum = -1;

        for (File arq : arquivos) {
            if (arq.isFile()) {
                BCP bcp = new BCP();

                try (BufferedReader br = new BufferedReader(new FileReader(arq))) {
                    String linhaArq;
                    String[] codigo = new String[TAM_MAX_PROGRAMA];
                    int i = 0;

                    String nome = br.readLine(); // primeira linha do arquivo
                    bcp.setNomeProcesso(nome);
                    while ((linhaArq = br.readLine()) != null) {
                        codigo[i] = linhaArq;
                        i++;
                    }
                    bcp.setCodigo(codigo);

                    if (arq.getName().equals("quantum.txt")) {
                        String q = new String(Files.readAllBytes(Paths.get(caminhoBase + arq.getName())));
                        quantum = Integer.parseInt(q.strip());
                    }
                    else{
                        Processo p = new Processo();
                        p.setBcp(bcp);
                        processosProntos.add(p);
                        tabProcessos.addProcesso(p);
                    }
                }
            }
        }
        return quantum;
    }

    

    public static void main(String[] args) throws IOException {
        Escalonador esc = new Escalonador();
        int quantum = carregaProcessosRAM();
        esc.setQuantum(quantum);
        
        while(esc.temProcessosBloq() || esc.temProcessosProntos()){
            if(esc.temProcessosProntos()){
                boolean parouAntes=false;
                int contQuantum=0;
                processoExec = processosProntos.remove(0);
                BCP processoExecBCP=processoExec.getBcp();
                processoExecBCP.setEstadoProcesso(BCP.EstadoProcesso.Executando);
                aumentaInterrompidos();
                while(contQuantum<quantum){
                    String instrucaoAtual=processoExecBCP.getCodigo()[processoExec.getBcp().getCP()];

                    if(instrucaoAtual.contains("X=")){
                        Registrador valor = new Registrador();
                        valor.setValor(Integer.parseInt(instrucaoAtual.substring(2, 3)));
                        processoExecBCP.setX(valor);
                    }
                    else if(instrucaoAtual.contains("Y=")){
                        Registrador valor = new Registrador();
                        valor.setValor(Integer.parseInt(instrucaoAtual.substring(2, 3)));
                        processoExecBCP.setY(valor);
                    }
                    else if (instrucaoAtual.contains("E/S")){
                        processoExecBCP.setEstadoProcesso(BCP.EstadoProcesso.Bloqueado);
                        processosBloq.add(processoExec);
                        processoExecBCP.setEspera(3);
                        parouAntes=true;
                    }
                    else if (instrucaoAtual.contains("COM")){
                      
                    }
                    else if (instrucaoAtual.contains("SAIDA")){
                        processosProntos.remove(processoExec);
                        tabProcessos.removeProcesso(processoExec);
                        parouAntes=true;
                        break;
                    }
                    processoExecBCP.aumentaCP();
                    contQuantum++;
                }
                if(parouAntes == false){
                    processoExecBCP.setEstadoProcesso(BCP.EstadoProcesso.Pronto);
                    processosProntos.add(processoExec);
                }
                diminuiEspera();
            }else{
                diminuiEspera();
            }
        }
        // Escalonador esc = new Escalonador(3);
        // Processo p1 = new Processo();
        // Processo p2 = new Processo();
        // Processo p3 = new Processo();
        // p1.nome = "A";
        // p2.nome = "B";
        // p3.nome = "C";

        // List<Processo> processos = new ArrayList<Processo>();
        // processos.add(p3);
        // processos.add(p1);
        // processos.add(p2);

        // esc.tabProcessos.setProcessos(processos);
        // esc.tabProcessos.exibeProcessos();
    }
    static public void aumentaInterrompidos(){
        for(Processo bloq: processosBloq){
            bloq.getBcp().aumentaInterrompidos();
        }
        verificaInterrompidos();
    }
    static public void verificaInterrompidos(){
        for (int i=0;i<processosBloq.size();i++){
            Processo bloq=processosBloq.get(i);
            if(bloq.getBcp().doisInterrompidos()){
                processosBloq.remove(bloq);
                bloq.getBcp().setEstadoProcesso(BCP.EstadoProcesso.Pronto);
                processosProntos.add(bloq);
            }
        }
    }
    static public void diminuiEspera( ){
        for(Processo bloq: processosBloq){
            if(bloq.getBcp().getEspera() > 0){
                bloq.getBcp().setEspera(bloq.getBcp().getEspera()-1);
            }
        }
        verificaEsperaZerada();
    }
    static public void verificaEsperaZerada(){
        for (int i=0;i<processosBloq.size();i++){
            Processo bloq=processosBloq.get(i);
            processosBloq.remove(bloq);
            bloq.getBcp().setEstadoProcesso(BCP.EstadoProcesso.Pronto);
            processosProntos.add(bloq);
        }
    }

}
