import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Sistema {
    private final int TAM_MAX_ARRANJO = 100;
    static List<String> RC;
    static Thread[] threads;

    public Sistema() {
        RC = new ArrayList<String>();
        threads = new Thread[TAM_MAX_ARRANJO];
    }

    private void carregaEstruturaRAM() throws IOException {
        File arq = new File("./dados/bd.txt"); // arquivo alvo

        if (arq.isFile()) {
            try (BufferedReader br = new BufferedReader(new FileReader(arq))) {
                String linhaArq;

                while ((linhaArq = br.readLine()) != null) {
                    RC.add(linhaArq);
                }
            } catch (Exception e) {
                System.out.println("Erro na leitura dos arquivos");
            }
        }
    }

    private int buscaPosicao(ThreadLocalRandom generator) {
        int pos = generator.nextInt(100);

        while (threads[pos] != null) { // verifica se a posição no arranjo de threads já está ocupada
            pos = generator.nextInt(100);
        }
        return pos;
    }

    private void populaObjetoThreads(int qntdLeitores, int qntdEscritores) {
        int totalObjetos = qntdLeitores + qntdEscritores;
        if (totalObjetos != 100) {
            System.out.println("Quantidade de objetos inválida, o total de objetos deve ser igual a 100");
        }

        ThreadLocalRandom generator = ThreadLocalRandom.current();

        for (int i = 0; i < qntdLeitores; i++) {
            int pos = buscaPosicao(generator);

            threads[pos] = new Leitor(RC);
        }

        for (int i = qntdLeitores; i < totalObjetos; i++) {
            int pos = buscaPosicao(generator);

            threads[pos] = new Escritor(RC);
        }
    }

    private void executaThreads() {
        int leitores = 0;
        int escritores = 0;
        for (int i = 0; i < threads.length; i++) {
            if (threads[i] != null) {
                if (threads[i].getClass().getSimpleName().equals("Leitor")) { // IF else utilizado para fins de debug e
                                                                              // entendimento, podem remover dps
                    leitores++;
                } else {
                    escritores++;
                }
                threads[i].start();

            } else {
                System.out.println("Thread não existe");
            }
        }

        System.out.println("Leitores: " + leitores);
        System.out.println("Escritores: " + escritores);
        System.out.println("Total: " + (leitores + escritores));
    }

    public static void main(String[] args) throws IOException {
        Sistema sis = new Sistema();

        sis.carregaEstruturaRAM();
        System.out.println(RC.size());

        sis.populaObjetoThreads(97, 3);
        sis.executaThreads();
    }
}
