package com.nickmafra.chat;

import javax.swing.*;
import java.awt.*;

public class JanelaChat {

    private final JFrame frame;
    private final JTextArea textoSaida;
    private final JTextField textoEntrada;
    private int width = 400;
    private int height = 600;

    public JanelaChat() {
        frame = new JFrame("Chat");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        textoSaida = createTextoSaida();
        textoSaida.setText("Bem vindo ao chat\nAinda sem comportamento");
        textoEntrada = new JTextField();
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

    public void start() {
        frame.setSize(width, height);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new JanelaChat().start();
    }
}
