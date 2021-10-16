import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class LogWriter {
    File arq;
    BCP bcp = new BCP();

    public LogWriter() {
    }

    public void criaArquivo(int quantumLido) {
        try {
            String quantumStr = Integer.toString(quantumLido);
            int tam = quantumStr.length();

            if (tam < 2)
                quantumStr = "0" + quantumStr;

            File arq = new File(String.format("./logs/log%s.txt", quantumStr));
             
            if (arq.createNewFile()) 
            {
                this.arq = arq;
            } 
            else 
            {
                System.out.println("Arquivo já existente, gerando um novo...");
                arq.delete();
                this.arq = new File(String.format("./logs/log%s.txt", quantumStr));
            }

            bcp.setNomeArquivoLog(this.arq.getPath());
        } 
        catch (IOException e) 
        {
            System.out.println("erro de E/S.");
        }
    }

    public void fechaWriter(FileWriter fw, BufferedWriter bw) throws IOException {
        bw.close();
        fw.close();
    }


    private void escreveString(String str) throws IOException {
        FileWriter fw = new FileWriter(bcp.getNomeArquivoLog(), true);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write(str);
        bw.newLine();
        fechaWriter(fw, bw);
    }

    void escreveCarregando(String nomeProcesso, File arquivoLeitura) throws IOException {
        String str = "Carregando " + nomeProcesso;

        escreveString(str);
    }

    void escreveES(String nomeProcesso) throws IOException {
        String str = "E/S iniciada em " + nomeProcesso;

        escreveString(str);
    }

    void escreveInterrupcao(String nomeProcesso, int instrucoesExecutadas) throws IOException {
        String str = "Interrompendo " + nomeProcesso + " após " + String.format("%d", instrucoesExecutadas)
                + " instruções";

        escreveString(str);
    }

    void escreveFinalizou(String nomeProcesso, int regX, int regY) throws IOException {
        String str = nomeProcesso + " terminado. " + "X=" + String.format("%d", regX) + ". Y="
                + String.format("%d", regY);

        escreveString(str);
    }

    void escreveExecutando(String nomeProcesso) throws IOException {
        String str = "Executando " + nomeProcesso;

        escreveString(str);
    }

    void escreveMediaTrocas(float mediaTrocas) throws IOException {
        String str = "MÉDIA DE TROCAS: " + String.format("%.1f", mediaTrocas);

        escreveString(str);
    }

    void escreveMediaIntrucoesPorQuantum(float mediaIntrucoesPorQuantum) throws IOException {
        String str = "MÉDIA DE INTRUÇÕES: " + String.format("%.1f", mediaIntrucoesPorQuantum);

        escreveString(str);
    }

    void escreveQuantum(int quantum) throws IOException {
        String str = "QUANTUM: " + quantum;

        escreveString(str);
    }

}
