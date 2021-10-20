
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Escalonador {

    private final int NULL = -1;
    private final int TAM_MAX_PROGRAMA = 21;
    private int quantum;

    private final TabelaDeProcessos tabProcessos;
    private final List<Processo> processosProntos;
    private final List<Processo> processosBloq;
    private Processo processoExec;

    private final LogWriter logWriter;

    public int getQuantum() {
        return quantum;
    }

    public boolean temProcessosProntos() {
        return processosProntos.size() > 0;
    }

    public boolean temProcessosBloq() {
        return processosBloq.size() > 0;
    }

    public Escalonador() {
        tabProcessos = new TabelaDeProcessos();
        processosProntos = new ArrayList<Processo>();
        processosBloq = new ArrayList<Processo>();
        logWriter = new LogWriter();
    }

    private boolean isQuantumValido(int quantumLido) {
        return quantumLido > 0;
    }

    private int carregaProcessosRAM() throws IOException {
        String caminhoBase = "./programas";
        File dir = new File(caminhoBase); // diretorio alvo
        File[] arquivos = dir.listFiles();
        File arqQuantum = new File("./programas/quantum.txt");
        int quantumLido = Integer.parseInt(new String(Files.readAllBytes(Paths.get(arqQuantum.toURI()))).trim());

        if (!isQuantumValido(quantumLido)) {
            return NULL;
        }

        logWriter.criaArquivo(quantumLido);
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

                    if (!arq.getName().equals("quantum.txt")) {
                        // escrevendo nome do processo
                        logWriter.escreveCarregando(nome);

                        Processo p = new Processo();

                        bcp.setX(new Registrador());
                        bcp.setY(new Registrador());

                        p.setBcp(bcp);
                        processosProntos.add(p);
                        tabProcessos.addProcesso(p);

                    }
                } catch (Exception e) {
                    System.out.println("Erro na leitura dos arquivos");
                }
            }
        }

        return quantumLido;
    }

    public void execute() throws IOException {
        int quantumLido = carregaProcessosRAM();

        if (quantumLido == NULL) {
            System.out.println("Quantum inválido");
            return;
        }
        this.quantum = quantumLido;

        Report report = new Report(processosProntos.size());

        while (this.temProcessosBloq() || this.temProcessosProntos()) {
            if (this.temProcessosProntos()) {

                processoExec = processosProntos.remove(0);

                // contador para quantidades de instruções executadas antes de sofrer a
                // interrupção seja por quantum ou por E/S
                int instrucoesExecutadasNoQuantum = 0;

                boolean parouAntes = false;
                int contQuantum = 0;

                BCP processoExecBCP = processoExec.getBcp();

                processoExecBCP.setEstadoProcesso(BCP.EstadoProcesso.Executando);
                String nomeProcesso = processoExecBCP.getNomeProcesso();

                // escrevendo o nome do processo em execução no arquivo
                logWriter.escreveExecutando(nomeProcesso);

                while (contQuantum < quantumLido) {

                    // incrementando a quantidade de intruções executadas para report
                    report.instrucaoExecutada();

                    instrucoesExecutadasNoQuantum++;

                    String instrucaoAtual = processoExecBCP.getCodigo()[processoExec.getBcp().getCP()];

                    if (instrucaoAtual.contains("X=") || instrucaoAtual.contains("Y=")) {
                        Registrador valor = new Registrador();

                        String[] atribuicao = instrucaoAtual.split("=");
                        valor.setValor(Integer.parseInt(atribuicao[1]));

                        if (atribuicao[0].equals("X")) {
                            processoExecBCP.setX(valor);
                        } else {
                            processoExecBCP.setY(valor);
                        }

                    } else if (instrucaoAtual.contains("E/S")) {

                        // escrevendo processo que executa E/S
                        logWriter.escreveES(nomeProcesso);

                        processoExecBCP.setEstadoProcesso(BCP.EstadoProcesso.Bloqueado);
                        processosBloq.add(processoExec);

                        processoExecBCP.setEspera(3);
                        parouAntes = true;

                        // escrevendo qnt de instruções executadas antes da interrupção de E/S
                        logWriter.escreveInterrupcao(nomeProcesso, instrucoesExecutadasNoQuantum);

                        processoExecBCP.aumentaCP();
                        break;

                    } else if (instrucaoAtual.contains("COM")) {

                    } else if (instrucaoAtual.contains("SAIDA")) {
                        processosProntos.remove(processoExec);
                        tabProcessos.removeProcesso(processoExec);
                        parouAntes = true;

                        this.logWriter.escreveFinalizou(nomeProcesso, processoExecBCP.getX(), processoExecBCP.getY());
                        break;
                    }

                    processoExecBCP.aumentaCP();
                    contQuantum++;

                } // fim quantum do processo

                if (parouAntes == false) {
                    processoExecBCP.setEstadoProcesso(BCP.EstadoProcesso.Pronto);
                    processosProntos.add(processoExec);
                    logWriter.escreveInterrupcao(nomeProcesso, instrucoesExecutadasNoQuantum);
                }

                // incrementando o contador de troca de processos
                report.trocaProcesso();

            }

            trataProcessosBloqueados();

        } // fim processos para executar

        // escrevendo o relatório de médias e o quantum utilizado
        logWriter.escreveMediaTrocas(report.mediaTrocaDeProcessos());
        logWriter.escreveMediaIntrucoesPorQuantum(report.mediaIntrucoesPorQuantum());
        logWriter.escreveQuantum(this.quantum);
    }

    private void trataProcessosBloqueados() {

        for (Iterator<Processo> iterator = processosBloq.iterator(); iterator.hasNext();) {
            Processo processoBloqueado = iterator.next();
            processoBloqueado.getBcp().setEspera(processoBloqueado.getBcp().getEspera() - 1);
            if (processoBloqueado.getBcp().getEspera() == 0) {
                iterator.remove();
                processoBloqueado.getBcp().setEstadoProcesso(BCP.EstadoProcesso.Pronto);
                processosProntos.add(processoBloqueado);
            }
        }
    }

    public static void main(String[] args) {
        Escalonador esc = new Escalonador();
        try {
            esc.execute();
        } catch (IOException ex) {
            System.out.println("erro de IO durante execução do escalonador");
        }
    }

}
