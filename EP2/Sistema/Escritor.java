import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Escritor extends Thread {
    List<String> RC;

    public Escritor() {
        this.RC = new ArrayList<String>();
    }

    public Escritor(List<String> RC) {
        this.RC = RC;
    }

    public void run() {
        try {
            escreve();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void escreve() throws InterruptedException {
        ThreadLocalRandom generator = ThreadLocalRandom.current();

        for (int i = 0; i < 100; i++) {
            int pos = generator.nextInt(100);

            RC.set(pos, "MODIFICADO");
        }
        Escritor.sleep(1);
    }
}
