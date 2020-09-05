package com.nickmafra.chat;

import java.io.ByteArrayOutputStream;
import java.util.function.Consumer;

public class StringConsumerOutputStream extends ByteArrayOutputStream {

    private final Consumer<String> consumer;

    public StringConsumerOutputStream(Consumer<String> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void flush() {
        consumer.accept(toString());
        reset();
    }
}
