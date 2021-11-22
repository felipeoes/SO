import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Sistema {
    private final static int TAM_MAX_ARRANJO = 100;
    private final static int TAM_REPETICAO_PROP = 50;
    private static int numeroLeitores = 100;
    private static int numeroEscritores = 0;
    static List<String> RC;
    static Thread[] threads;
    static float mediaTempo;

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

    private void populaObjetoThreads(int qntdLeitores, int qntdEscritores, int opcaoLeituraEscrita) {
        int totalObjetos = qntdLeitores + qntdEscritores;
        if (totalObjetos != 100) {
            System.out.println("Quantidade de objetos inválida, o total de objetos deve ser igual a 100");
            return;
        }

        ThreadLocalRandom generator = ThreadLocalRandom.current();

        for (int i = 0; i < qntdLeitores; i++) 
        {
            int pos = buscaPosicao(generator);
            threads[pos] = new Leitor(RC, opcaoLeituraEscrita);
        }

        for (int i = qntdLeitores; i < totalObjetos; i++) 
        {
            int pos = buscaPosicao(generator);
            threads[pos] = new Escritor(RC, opcaoLeituraEscrita);
        }
    }

    private void executaThreads() {
        for (int i = 0; i < threads.length; i++) {
            if (threads[i] != null) 
            {
                threads[i].start();
            } 
            else 
            {
                System.out.println("Thread não existe");
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        long horaInicioPrograma = System.currentTimeMillis();
        for (int contador = 0; contador < 2; contador++) 
        {
			for (int i = 0; i <= TAM_MAX_ARRANJO; i++) 
            {
				for (int j = 0; j <= TAM_REPETICAO_PROP; j++) 
                {
                    if(numeroEscritores < 0 || numeroEscritores > 100) 
                    {
                        numeroEscritores = 0;
                    }
                    if(numeroLeitores < 0 || numeroLeitores > 100)
                    {
                        numeroLeitores = 100;
                    }
                    Sistema sis = new Sistema();
                    sis.carregaEstruturaRAM();
					sis.populaObjetoThreads(numeroLeitores, numeroEscritores, contador + 1);
					long horaInicioThread = System.currentTimeMillis();              
					sis.executaThreads();
					long horaFim = System.currentTimeMillis();
					mediaTempo += horaFim - horaInicioThread;
                    numeroEscritores++;
                    numeroLeitores--;
                    
				}
				mediaTempo /= TAM_REPETICAO_PROP;
				System.out.println(mediaTempo);

            }
			long fimPrograma = System.currentTimeMillis();
			System.out.println("Tempo de execução: " + ((fimPrograma - horaInicioPrograma) / 60000) + " minutos");
		}     
        System.out.println(RC.size());
    }
}
