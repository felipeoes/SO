import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Escritor extends Thread {
    List<String> RC;
    private Locker locker = new Locker();
    private int opcaoLeituraEscrita;
    boolean temEscritor = false;
	int quantidadeLeitor = 0;

    public Escritor() {
        this.RC = new ArrayList<String>();
    }

    public Escritor(List<String> RC, int opcaoLeituraEscrita) {
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

    private void semEscritorLeitor() throws InterruptedException
    {
		locker.block();
		escreveBD();
		locker.desblock();
	}

	private void comEscritorLeitor() throws InterruptedException
    {
		validaEscreve();
		escreveBD();
		paraEscreve();
	}

    private void escreveBD() throws InterruptedException {
        ThreadLocalRandom generator = ThreadLocalRandom.current();

        for (int i = 0; i < 100; i++) {
            int pos = generator.nextInt(100);
            RC.set(pos, "MODIFICADO");
        }
        Thread.sleep(1);
    }

    synchronized void validaEscreve() 
    {
		while (!regraEscrever()) 
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
	}
    
	synchronized void paraEscreve() 
    {
		temEscritor = false;
		notifyAll();
	}

    boolean regraEscrever() 
    {
        return quantidadeLeitor == 0 && !temEscritor;
	}
}
