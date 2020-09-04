package com.nickmafra.chat;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

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
