package com.nickmafra.chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;

public class Chat {

    private final PrintStreamScanner consolePss;
    private PrintStreamScanner socketPss;

    private boolean isHost;
    private int port;

    public Chat(OutputStream out, InputStream in) {
        this.consolePss = new PrintStreamScanner(out, in);
    }

    public void start() {
        consolePss.println("Bem vindo ao chat.");
        try {
            isHost = consolePss.getSimNao("Diga se deseja ser o hospedeiro (sim/nao)");
            if (isHost) {
                esperarConexao();
            } else {
                conectar();
            }
            conversar();
        } catch (InterruptedIOException e) {
            consolePss.println("Conexao interrompida.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void esperarConexao() throws IOException {
        port = consolePss.getPositiveInt("Digite a porta", 8029);
        ServerSocket serverSocket = new ServerSocket(port);
        consolePss.println("Esperando cliente conectar...");
        Socket socket = serverSocket.accept();
        setupConexao(socket);
    }

    private void conectar() throws IOException {
        String hostname = consolePss.getString("Digite o endereco", "localhost");
        port = consolePss.getPositiveInt("Digite a porta", 8029);
        consolePss.println("Conectando com o servidor...");
        Socket socket = new Socket(hostname, port);
        setupConexao(socket);
    }

    private void setupConexao(Socket socket) throws IOException {
        consolePss.println("Conectado com: " + socket.getRemoteSocketAddress());
        socketPss = new PrintStreamScanner(socket);
    }

    private void conversar() throws InterruptedIOException {
        consolePss.println("---Inicio da conversa---");
        while (!Thread.currentThread().isInterrupted()) {
            if (isHost) {
                enviar();
                receber();
            } else {
                receber();
                enviar();
            }
        }
    }

    private void enviar() {
        consolePss.println();
        String textoOut = consolePss.getString(textoPessoaMensagem(true), null);
        socketPss.println(textoOut);
    }

    private void receber() throws InterruptedIOException {
        consolePss.print("Aguarde a outra pessoa digitar...");
        String textoIn;
        try {
            textoIn = socketPss.nextLine();
        } catch (NoSuchElementException e) {
            throw new InterruptedIOException();
        } finally {
            consolePss.println("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
        }
        consolePss.println(textoPessoaMensagem(false) + ": " + textoIn);
    }

    private String textoPessoaMensagem(boolean isEnvio) {
        return (isEnvio == isHost ? "- Hospedeiro" : "- Cliente")
                + (isEnvio ? " (voce)" : "");
    }

    public static void main(String[] args) {
        new Chat(System.out, System.in).start();
    }
}
