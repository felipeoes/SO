import java.util.concurrent.ThreadLocalRandom;

public class Sistema extends Thread {

    private final static int TAM_MAX_ARRANJO = 100;
    private final static int TAM_REPETICAO_PROP = 50;

    private static int numeroLeitores = 100;
    private static int numeroEscritores = 0;

    public float mediaTempo;

    private Thread[] threads;
    private Database database;

    public Sistema(Database database) {
        this.database = database;
        this.threads = new Thread[100];
    }

    private int buscaPosicao(ThreadLocalRandom generator) {
        int pos = generator.nextInt(100);
        while (threads[pos] != null) {
            pos = generator.nextInt(100);
        }
        return pos;
    }

    private void populaObjetoThreads(int qntdLeitores, int qntdEscritores) {
        int totalObjetos = qntdLeitores + qntdEscritores;
        if (totalObjetos != 100) {
            System.out.println("Quantidade de objetos inválida, o total de objetos deve ser igual a 100");
            return;
        }

        ThreadLocalRandom generator = ThreadLocalRandom.current();

        for (int i = 0; i < qntdLeitores; i++) {
            int pos = buscaPosicao(generator);
            threads[pos] = new Leitor(database);
        }

        for (int i = qntdLeitores; i < totalObjetos; i++) {
            int pos = buscaPosicao(generator);
            threads[pos] = new Escritor(database);
        }
    }

    private void executaThreads() {
        for (int i = 0; i < threads.length; i++) {
            if (threads[i] != null) {
                threads[i].start();
            } else {
                System.out.println("Thread não existe");
            }
        }
        for (int i = 0; i < threads.length; i++) {
            if (threads[i] != null) {
                try {
                    threads[i].join();
                } catch (InterruptedException ex) {
                    System.err.println("Erro no Join");
                }
            } else {
                System.out.println("Thread não existe");
            }
        }
    }

    @Override
    public void run() {

        System.out.println("SOLUÇÃO COM LEITOR-ESCRITOR - Experimentos iniciados");
        long tempoInicioPrograma = System.currentTimeMillis();

        long tempoMinimo = 0;
        long tempoMaximo = 0;

        for (int i = 0; i <= TAM_MAX_ARRANJO; i++) {

            for (int j = 0; j < TAM_REPETICAO_PROP; j++) {

                Sistema sis = new Sistema(database);
                sis.populaObjetoThreads(numeroLeitores, numeroEscritores);
                long tempoInicioThread = System.currentTimeMillis();
                sis.executaThreads();
                long tempoFimThread = System.currentTimeMillis();

                long tempoExperimento = tempoFimThread - tempoInicioThread;

                mediaTempo += tempoExperimento;

                if (tempoExperimento > tempoMaximo) {
                    tempoMaximo = tempoExperimento;
                } else if (tempoExperimento < tempoMinimo) {
                    tempoMinimo = tempoExperimento;
                } else if (tempoMinimo == 0) {
                    tempoMinimo = tempoExperimento;
                }
                
            }

            mediaTempo /= TAM_REPETICAO_PROP;
            System.out.println("proporção leitor-escritor: " + numeroLeitores + "-" +
                    + numeroEscritores + "; Média do tempo de execução(ms): " + mediaTempo
                    + "; Tempo mínimo atingido(ms): " + tempoMinimo
                    + "; Tempo máximo atingido(ms): " + tempoMaximo
            );

            numeroEscritores++;
            numeroLeitores--;
            mediaTempo = 0;
            tempoMinimo = 0;
            tempoMaximo = 0;
        }

        long tempoFimPrograma = System.currentTimeMillis();
        System.out.println("Tempo de execução total: " + ((tempoFimPrograma - tempoInicioPrograma) / 60000) + " minutos");

    }
}
