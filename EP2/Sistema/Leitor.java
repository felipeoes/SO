import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Leitor extends Thread {
    List<String> RC;

    public Leitor() {
        this.RC = new ArrayList<String>();
    }

    public Leitor(List<String> RC) {
        this.RC = RC;
    }

    public void run() {
        try {
            le();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void le() throws InterruptedException {
        String palavraLida;

        ThreadLocalRandom generator = ThreadLocalRandom.current();

        for (int i = 0; i < 100; i++) {
            int pos = generator.nextInt(100);

            palavraLida = RC.get(pos);
            System.out.println("PALAVRA LEITOR: " + palavraLida); // debug
        }
        Leitor.sleep(1);

    }
}