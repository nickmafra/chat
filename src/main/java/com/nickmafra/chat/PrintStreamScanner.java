package com.nickmafra.chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Classe de conveniência que recebe um OutputStream e um InputStream e
 * disponibiliza métodos para que se possa interagir com o usuário com
 * mais eficiência de código.
 */
public class PrintStreamScanner {

    private final OutputStream out;
    private final InputStream in;
    private final PrintStream print;
    private final Scanner scanner;

    private boolean printValorDigitado;

    public PrintStreamScanner(OutputStream out, InputStream in) {
        this.out = out;
        this.in = in;
        this.print = new PrintStream(out, true);
        this.scanner = new Scanner(in);
    }

    public PrintStreamScanner(Socket socket) throws IOException {
        this(socket.getOutputStream(), socket.getInputStream());
    }

    public OutputStream getOut() {
        return out;
    }

    public InputStream getIn() {
        return in;
    }

    public PrintStream getPrint() {
        return print;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void setPrintValorDigitado(boolean printValorDigitado) {
        this.printValorDigitado = printValorDigitado;
    }

    public void print(Object obj) {
        print.print(obj);
    }

    public void println(Object obj) {
        print.println(obj);
    }

    public void println() {
        print.println();
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
            if (msg != null) {
                print.print(msg);
                if (fb != null) {
                    print.print(" (" + fb + ")");
                }
                print.print(": ");
            }
            value = scanner.nextLine();
            if (fb != null && value.isEmpty()) {
                value = fb.toString();
            }
        } while (value.isEmpty());
        if (printValorDigitado) {
            print.println(value);
        }
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
                print.println("Numero invalido.");
            }
            if (value != null && value <= 0) {
                print.println("Numero deve ser maior que zero.");
                value = null;
            }
        } while (value == null);
        return value;
    }

    public String nextLine() {
        return scanner.nextLine();
    }
}
