import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Leitor extends Thread {
    List<String> RC;
    private Locker locker = new Locker();
    private int opcaoLeituraEscrita;
    boolean temEscritor = false;
	int quantidadeLeitor = 0;

    public Leitor() {
        this.RC = new ArrayList<String>();
    }

    public Leitor(List<String> RC, int opcaoLeituraEscrita) {
        this.RC = RC;
        this.opcaoLeituraEscrita = opcaoLeituraEscrita;
    }

    @Override
    public void run() 
    {
        if (opcaoLeituraEscrita == 0) 
        {
            try 
            {
                comEscritorLeitor();
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
        } 
        else 
        {
            try 
            {
                semEscritorLeitor();
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
        }
    }

    	
	private void semEscritorLeitor() throws InterruptedException {
		locker.block();
		leBD();
		locker.desblock();
	}

	
	private void comEscritorLeitor() throws InterruptedException {
		validaLeitura();
		leBD();
		paraLeitura();
	}


    public void leBD() throws InterruptedException {
        String palavraLida;

        ThreadLocalRandom generator = ThreadLocalRandom.current();

        for (int i = 0; i < 100; i++) {
            int pos = generator.nextInt(100);

            palavraLida = RC.get(pos);
            System.out.println("PALAVRA LEITOR: " + palavraLida); // debug
        }
        Leitor.sleep(1); 
    }


    boolean regraLeitura() 
    {
		return !temEscritor;
	}

	synchronized void validaLeitura() 
    {
		while (!regraLeitura()) 
        {
			try 
            {
				wait();
			}
            catch (InterruptedException e) 
            {
				e.printStackTrace();
			}
		}
		quantidadeLeitor += 1;
	}

	synchronized void paraLeitura() 
    {
		quantidadeLeitor -=1;
		notifyAll();
	}
}