package com.nickmafra.chat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class ActionPerformed extends AbstractAction {

    private final Consumer<ActionEvent> consumer;

    ActionPerformed(Consumer<ActionEvent> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        consumer.accept(e);
    }
}
