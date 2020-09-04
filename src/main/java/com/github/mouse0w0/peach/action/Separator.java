package com.github.mouse0w0.peach.action;

public class Separator extends Action {

    private static final Separator INSTANCE = new Separator();

    public static Separator getInstance() {
        return INSTANCE;
    }

    @Override
    public void perform(ActionEvent event) {
        throw new UnsupportedOperationException();
    }
}
