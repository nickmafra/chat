package com.nickmafra.chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Chat {

    private final PrintStreamScanner console;

    private boolean isHost;
    private int port;
    private Socket socket;

    private PrintStream socketOut;
    private Scanner socketIn;

    public Chat(OutputStream out, InputStream in) {
        this.console = new PrintStreamScanner(out, in);
    }

    public void start() {
        console.println("Bem vindo ao chat.");
        try {
            isHost = console.getSimNao("Diga se deseja ser o hospedeiro (sim/nao)");
            if (isHost) {
                esperarConexao();
            } else {
                conectar();
            }
            conversar();
        } catch (InterruptedIOException e) {
            console.println("Conexão interrompida.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void esperarConexao() throws IOException {
        port = console.getPositiveInt("Digite a porta", 8029);
        ServerSocket serverSocket = new ServerSocket(port);
        console.println("Esperando cliente conectar...");
        socket = serverSocket.accept();
        setupConexao();
    }

    private void conectar() throws IOException {
        String hostname = console.getString("Digite o endereço", "localhost");
        port = console.getPositiveInt("Digite a porta", 8029);
        console.println("Esperando servidor aceitar...");
        socket = new Socket(hostname, port);
        setupConexao();
    }

    private void setupConexao() throws IOException {
        console.println("Conectado com: " + socket.getRemoteSocketAddress());
        socketIn = new Scanner(socket.getInputStream());
        socketOut = new PrintStream(socket.getOutputStream());
    }

    private void conversar() throws InterruptedIOException {
        console.println("---Início da conversa---");
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
        console.println();
        String textoOut = console.getString("- Você", null);
        socketOut.println(textoOut);
    }

    private void receber() throws InterruptedIOException {
        console.println();
        console.println("Aguarde a outra pessoa digitar...");
        String textoIn;
        try {
            textoIn = socketIn.nextLine();
        } catch (NoSuchElementException e) {
            throw new InterruptedIOException();
        }
        console.println("- Fulano: " + textoIn);
    }

    public static void main(String[] args) {
        new Chat(System.out, System.in).start();
    }
}
