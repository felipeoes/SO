package javaapplication3;

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
    public TabelaDeProcessos(){
        processos=new ArrayList<Processo>();
    }
    // private void buscaProcesso(BCP bcp) {
    //     for(Processo p : processos) {
    //         if(bcp.equals(processos) {
    //             return Processo
    //         }
    //     }
    //     return Processo
    // }
    // public void adicionaProcesso(BCP bcp) {
    //     Processo p = buscaProcesso(bcp);

    // }

    // public void exibeProcessos() {
    //     for (Processo p : processos) {
    //         System.out.println(p.nome);
    //     }
    // }
}
