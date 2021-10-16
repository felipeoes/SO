import java.util.*;

public class TabelaDeProcessos {
    private List<Processo> processos;

    public List<Processo> getProcessos() {
        return processos;
    }

    public void setProcessos(List<Processo> processos) {
        this.processos = processos;
    }

    public void removeProcesso(Processo processo) {
        this.processos.remove(processo);
    }

    public void addProcesso(Processo processo) {
        this.processos.add(processo);
    }

    public TabelaDeProcessos() {
        processos = new ArrayList<Processo>();
    }
}
