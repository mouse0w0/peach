package com.github.mouse0w0.peach.action;

public abstract class Action {

    private Appearance appearance;

    public Appearance getAppearance() {
        if (appearance == null) {
            appearance = new Appearance();
        }
        return appearance;
    }

    public abstract void perform(ActionEvent event);

    public void update(ActionEvent event) {
        // Nothing to do.
    }
}
