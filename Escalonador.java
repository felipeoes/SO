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

    public int getQuantum() {
        return quantum;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
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
                }

                Processo p = new Processo();
                p.setBcp(bcp);
                processosProntos.add(p);
            }
        }

        return quantum;
    }

    

    public static void main(String[] args) throws IOException {
        Escalonador esc = new Escalonador();
        int quantum = carregaProcessosRAM();
        esc.setQuantum(quantum);
        

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

}