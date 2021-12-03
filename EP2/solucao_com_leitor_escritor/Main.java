import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Database database = new Database();
        try {
            database.carregaEstruturaRAM();
        } catch (IOException ex) {
            System.err.println("Erro ao carregar os arquivos da RAM");
        }

        Sistema t = new Sistema(database);
        t.start();
    }
}
