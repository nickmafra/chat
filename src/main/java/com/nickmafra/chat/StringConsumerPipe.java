package com.nickmafra.chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.function.Consumer;

public class StringConsumerPipe implements Consumer<String> {

    private final Pipe pipe;
    private final PrintStream printStream;

    public StringConsumerPipe() throws IOException {
        pipe = new Pipe();
        printStream = new PrintStream(pipe.getPipedOutputStream());
    }

    public InputStream getInputStream() {
        return pipe.getPipedInputStream();
    }

    @Override
    public void accept(String s) {
        printStream.print(s);
        printStream.flush();
    }
}
