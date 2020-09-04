package com.github.mouse0w0.peach.action;

public class ActionEvent {

    private final Object source;

    public ActionEvent(Object source) {
        this.source = source;
    }

    public Object getSource() {
        return source;
    }
}
