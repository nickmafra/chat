package com.nickmafra.chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class JanelaChat {

    private final JFrame frame;
    private final JTextArea textoSaida;
    private final JTextField textoEntrada;
    private int width = 400;
    private int height = 600;

    private Consumer<String> consumerTextoDigitado;

    public JanelaChat() {
        frame = new JFrame("Chat");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        textoSaida = createTextoSaida();
        textoSaida.setText("Bem vindo ao chat\n");
        textoEntrada = new JTextField();
        textoEntrada.setAction(new ActionPerformed(this::onEnter));
        JPanel panel = createPanel(textoSaida, textoEntrada);
        frame.add(panel);
    }

    private static JTextArea createTextoSaida() {
        JTextArea textoSaida = new JTextArea();
        textoSaida.setEditable(false);
        textoSaida.setBackground(Color.BLACK);
        textoSaida.setForeground(Color.GREEN);
        return textoSaida;
    }

    private static JPanel createPanel(Component textoSaida, Component textoEntrada) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(textoEntrada, BorderLayout.SOUTH);
        panel.add(textoSaida, BorderLayout.CENTER);
        return panel;
    }

    public JFrame getFrame() {
        return frame;
    }

    public synchronized void setConsumerTextoDigitado(Consumer<String> consumerTextoDigitado) {
        this.consumerTextoDigitado = consumerTextoDigitado;
    }

    public void start() {
        frame.setSize(width, height);
        frame.setVisible(true);
    }

    private synchronized void onEnter(ActionEvent e) {
        if (consumerTextoDigitado != null) {
            consumerTextoDigitado.accept(textoEntrada.getText());
            textoEntrada.setText("");
        }
    }

    public synchronized void print(String text) {
        textoSaida.setText(textoSaida.getText() + text);
    }

    public void println(String text) {
        print(text + "\n");
    }

    public static void main(String[] args) {
        JanelaChat janelaChat = new JanelaChat();
        janelaChat.setConsumerTextoDigitado(janelaChat::println);
        janelaChat.start();
    }
}
