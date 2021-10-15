package javaapplication3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Escalonador {

    private final static int TAM_MAX_PROGRAMA = 21;
    private int quantum;

    private static TabelaDeProcessos tabProcessos;
    private static List<Processo> processosProntos;
    private static List<Processo> processosBloq;
    private static Processo processoExec;

    private final LogPrinter logPrinter;

    public int getQuantum() {
        return quantum;
    }

    public boolean temProcessosProntos() {
        if (processosProntos.size() > 0) {
            return true;
        }
        return false;
    }

    public boolean temProcessosBloq() {
        if (processosBloq.size() > 0) {
            return true;
        }
        return false;
    }

    public Escalonador() {
        tabProcessos = new TabelaDeProcessos();
        processosProntos = new ArrayList<Processo>();
        processosBloq = new ArrayList<Processo>();
        logPrinter = new LogPrinter();
    }

    private int carregaProcessosRAM() throws IOException {
        String caminhoBase = "C:\\Users\\gabriel\\Desktop\\projeto_SO\\JavaApplication3\\src\\programas\\";
        File dir = new File(caminhoBase); // diretorio alvo
        File[] arquivos = dir.listFiles();
        int quantumLido = -1;

        // criando arquivo para teste
        logPrinter.createFile();

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
                        quantumLido = Integer.parseInt(q.trim());
                    } else {

                        //printando nome do processo
                        logPrinter.printCarregando(nome);

                        Processo p = new Processo();

                        bcp.setX(new Registrador());
                        bcp.setY(new Registrador());

                        p.setBcp(bcp);
                        processosProntos.add(p);
                        tabProcessos.addProcesso(p);

                    }
                }
            }
        }

        return quantumLido;
    }

    public void execute() throws IOException {

        int quantumLido = carregaProcessosRAM();
        this.quantum = quantumLido;

        Report report = new Report(processosProntos.size());

        while (this.temProcessosBloq() || this.temProcessosProntos()) {
            if (this.temProcessosProntos()) {

                processoExec = processosProntos.remove(0);

                // contador para quantidades de instruções executadas antes de sofrer a interrupção seja por quantum ou por E/S
                int instrucoesExecutadasNoQuantum = 0;

                boolean parouAntes = false;
                int contQuantum = 0;

                BCP processoExecBCP = processoExec.getBcp();

                processoExecBCP.setEstadoProcesso(BCP.EstadoProcesso.Executando);
                String nomeProcesso = processoExecBCP.getNomeProcesso();

                // printando o nome do processo em execução no arquivo
                this.logPrinter.printExecutando(nomeProcesso);

                while (contQuantum < quantumLido) {

                    //incrementando a quantidade de intruções executadas para report
                    report.instrucaoExecutada();

                    instrucoesExecutadasNoQuantum++;

                    String instrucaoAtual = processoExecBCP.getCodigo()[processoExec.getBcp().getCP()];

                    if (instrucaoAtual.contains("X=")) {
                        Registrador valor = new Registrador();
                        valor.setValor(Integer.parseInt(instrucaoAtual.substring(2, 3)));
                        processoExecBCP.setX(valor);
                    } else if (instrucaoAtual.contains("Y=")) {
                        Registrador valor = new Registrador();
                        valor.setValor(Integer.parseInt(instrucaoAtual.substring(2, 3)));
                        processoExecBCP.setY(valor);
                    } else if (instrucaoAtual.contains("E/S")) {

                        // printando processo que executa E/S
                        this.logPrinter.printES(nomeProcesso);

                        processoExecBCP.setEstadoProcesso(BCP.EstadoProcesso.Bloqueado);
                        processosBloq.add(processoExec);

                        processoExecBCP.setEspera(3);
                        parouAntes = true;

                        // printando qnt de instruções executadas antes da interrupção de E/S
                        this.logPrinter.printInterrupcao(nomeProcesso, instrucoesExecutadasNoQuantum);

                        processoExecBCP.aumentaCP();
                        break;

                    } else if (instrucaoAtual.contains("COM")) {

                    } else if (instrucaoAtual.contains("SAIDA")) {
                        processosProntos.remove(processoExec);
                        tabProcessos.removeProcesso(processoExec);
                        parouAntes = true;

                        this.logPrinter.printFinalizou(nomeProcesso, processoExecBCP.getX(), processoExecBCP.getY());
                        break;
                    }
                    processoExecBCP.aumentaCP();
                    contQuantum++;

                } // fim quantum do processo

                if (parouAntes == false) {
                    processoExecBCP.setEstadoProcesso(BCP.EstadoProcesso.Pronto);
                    processosProntos.add(processoExec);
                    this.logPrinter.printInterrupcao(nomeProcesso, instrucoesExecutadasNoQuantum);
                }

                // incrementando o contador de troca de processos
                report.trocaProcesso();

            }

            trataProcessosBloqueados();

        } // fim processos para executar

        // printando o relatório de médias e o quantum utilizado
        this.logPrinter.printMediaTrocas(report.mediaTrocaDeProcessos());
        this.logPrinter.printMediaIntrucoesPorQuantum(report.mediaIntrucoesPorQuantum());
        this.logPrinter.printQuantum(this.quantum);

        this.logPrinter.close();

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
            System.out.println("erro de IO");
        }
    }

}
