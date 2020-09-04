package com.nickmafra.chat;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Classe de conveniência que recebe um OutputStream e um InputStream e
 * disponibiliza métodos para que se possa interagir com o usuário com
 * mais eficiência de código.
 */
public class PrintStreamScanner {

    private final PrintStream consoleOut;
    private final Scanner consoleIn;

    public PrintStreamScanner(OutputStream out, InputStream in) {
        this.consoleOut = new PrintStream(out);
        this.consoleIn = new Scanner(in);
    }

    public PrintStream getConsoleOut() {
        return consoleOut;
    }

    public Scanner getConsoleIn() {
        return consoleIn;
    }

    public void print(Object obj) {
        consoleOut.print(obj);
    }

    public void println(Object obj) {
        consoleOut.println(obj);
    }

    public void println() {
        consoleOut.println();
    }

    /**
     * Pede para que o usuário digite um texto e retorna o texto digitado.
     *
     * @param msg mensagem pedindo para digitar algo
     * @param fb valor de fallback (default)
     * @return o texto digitado
     */
    public String getString(String msg, Object fb) {
        String value;
        do {
            consoleOut.print(msg);
            if (fb != null) {
                consoleOut.print(" (" + fb + ")");
            }
            consoleOut.print(": ");
            value = consoleIn.nextLine();
            if (fb != null && value.isEmpty()) {
                value = fb.toString();
            }
        } while (value.isEmpty());
        return value;
    }

    /**
     * Pede para que o usuário digite sim ou nao e retorna o boolean correspondente.
     *
     * @param msg mensagem pedindo para digitar algo
     * @return o boolean correspondente
     */
    public boolean getSimNao(String msg) {
        Boolean bValue = null;
        do {
            String value = getString(msg, null);
            if (value.equalsIgnoreCase("s") || value.equalsIgnoreCase("sim")) {
                bValue = true;
            } else if (value.equalsIgnoreCase("n") || value.equalsIgnoreCase("nao")) {
                bValue = false;
            }
        } while (bValue == null);
        return bValue;
    }

    /**
     * Pede para que o usuário digite um número positivo e retorna o número digitado.
     *
     * @param msg mensagem pedindo para digitar algo
     * @param fb valor de fallback (default)
     * @return o número digitado
     */
    public Integer getPositiveInt(String msg, Integer fb) {
        Integer value = null;
        do {
            String strValue = getString(msg, fb);
            try {
                value = Integer.parseInt(strValue);
            } catch (NumberFormatException e) {
                consoleOut.println("Número inválido.");
            }
            if (value != null && value <= 0) {
                consoleOut.println("Número deve ser maior que zero.");
                value = null;
            }
        } while (value == null);
        return value;
    }
}
