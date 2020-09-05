package com.nickmafra.chat;

import java.io.IOException;
import java.io.OutputStream;

public class JanelaAsyncChatMain {

    public static void main(String[] args) throws IOException {
        JanelaChat janelaChat = new JanelaChat();

        OutputStream out = new StringConsumerOutputStream(janelaChat::print);
        StringConsumerPipe pipe = new StringConsumerPipe();

        janelaChat.setConsumerTextoDigitado(pipe::accept);
        janelaChat.start();

        AsyncChat asyncChat = new AsyncChat(out, pipe.getInputStream());
        asyncChat.start();
    }

}
