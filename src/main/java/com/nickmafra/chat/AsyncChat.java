package com.nickmafra.chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;

public class AsyncChat {

    private final PrintStreamScanner consolePss;
    private PrintStreamScanner socketPss;

    private boolean isHost;
    private int port;
    private final String comandoSair = "sair";

    public AsyncChat(OutputStream out, InputStream in) {
        consolePss = new PrintStreamScanner(out, in);
    }

    public void start() {
        consolePss.println("Iniciando chat...");
        try {
            consolePss.setPrintValorDigitado(true);
            isHost = consolePss.getSimNao("Diga se deseja ser o hospedeiro (sim/nao)");
            if (isHost) {
                esperarConexao();
            } else {
                conectar();
            }
            exibirInstrucoes();
            conversar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void esperarConexao() throws IOException {
        consolePss.setPrintValorDigitado(true);
        port = consolePss.getPositiveInt("Digite a porta", 8029);
        ServerSocket serverSocket = new ServerSocket(port);
        consolePss.println("Esperando cliente conectar...");
        Socket socket = serverSocket.accept();
        setupConexao(socket);
    }

    private void conectar() throws IOException {
        consolePss.setPrintValorDigitado(true);
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

    private void exibirInstrucoes() {
        consolePss.println();
        consolePss.println("Para sair, digite '" + comandoSair + "'");
        consolePss.println("Quando nao for sua vez de digitar, espere.");
    }

    private void conversar() {
        consolePss.println();
        consolePss.println("--- Inicio da conversa ---");
        Thread threadEnvio = new Thread(this::manterEnviando, "Envio");
        Thread threadRecebimento = new Thread(this::manterRecebendo, "Recebimento");
        threadEnvio.start();
        threadRecebimento.start();
    }

    private void manterEnviando() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                enviar();
            } catch (InterruptedException e) {
                consolePss.println("Voce interrompeu a conexao.");
                Thread.currentThread().interrupt();
            }
        }
    }

    private void manterRecebendo() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                receber();
            } catch (InterruptedIOException e) {
                consolePss.println("A outra pessoa interrompeu a conexao.");
                Thread.currentThread().interrupt();
            }
        }
    }

    private void enviar() throws InterruptedException {
        consolePss.println();
        consolePss.setPrintValorDigitado(false);
        String textoOut = consolePss.getString(null, null);
        if (textoOut.equalsIgnoreCase(comandoSair)) {
            throw new InterruptedException();
        }
        socketPss.println(textoOut);
        consolePss.println(textoPessoaMensagem(true) + ": " + textoOut);
    }

    private void receber() throws InterruptedIOException {
        consolePss.println();
        String textoIn;
        try {
            textoIn = socketPss.nextLine();
        } catch (NoSuchElementException e) {
            throw new InterruptedIOException();
        }
        consolePss.println(textoPessoaMensagem(false) + ": " + textoIn);
    }

    private String textoPessoaMensagem(boolean isEnvio) {
        return (isEnvio == isHost ? "- Hospedeiro" : "- Cliente")
                + (isEnvio ? " (voce)" : "");
    }

}
