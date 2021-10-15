package javaapplication3;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class LogPrinter {

    private PrintWriter out;

    public LogPrinter() {
    }

    public void createFile() {
        try {
            File myObj = new File("C:\\Users\\gabriel\\Desktop\\projeto_SO\\JavaApplication3\\src\\log\\log_teste.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
            out = new PrintWriter("C:\\Users\\gabriel\\Desktop\\projeto_SO\\JavaApplication3\\src\\log\\log_teste.txt");
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    public void close() {
        out.close();
    }

    void printCarregando(String nomeProcesso) {
        out.println("Carregando " + nomeProcesso);
    }

    void printES(String nomeProcesso) {
        out.println("E/S iniciada em " + nomeProcesso);
    }

    void printInterrupcao(String nomeProcesso, int instrucoesExecutadas) {
        out.println("Interrompendo " + nomeProcesso + " após " + String.format("%d", instrucoesExecutadas) + " instruções");
    }

    void printFinalizou(String nomeProcesso, int regX, int regY) {
        out.println(nomeProcesso + " terminado. " + "X=" + String.format("%d", regX) + ". Y=" + String.format("%d", regY));
    }

    void printExecutando(String nomeProcesso) {
        out.println("Executando " + nomeProcesso);
    }

    void printMediaTrocas(float mediaTrocas) {
        out.println("MÉDIA DE TROCAS: " + String.format("%.1f", mediaTrocas));
    }

    void printMediaIntrucoesPorQuantum(float mediaIntrucoesPorQuantum) {
        out.println("MÉDIA DE INTRUÇÕES: " + String.format("%.1f", mediaIntrucoesPorQuantum));
    }

    void printQuantum(int quantum) {
        out.println("QUANTUM: " + quantum);
    }

}
