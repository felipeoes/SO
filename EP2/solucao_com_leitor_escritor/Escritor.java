public class Escritor extends Thread {

    private final Database database;

    public Escritor(Database database) {
        this.database = database;

    }

    @Override
    public void run() {
        try {
            this.database.write();
        } catch (InterruptedException ex) {
            System.err.println("Erro Escritor");
        }
    }

}
