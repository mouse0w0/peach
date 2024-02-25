package com.github.mouse0w0.peach.action;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class ActionGroup extends Action {
    private boolean popup;

    public boolean isPopup() {
        return popup;
    }

    public void setPopup(boolean popup) {
        this.popup = popup;
    }

    public abstract List<Action> getChildren(@Nullable ActionEvent event);

    @Override
    public void perform(ActionEvent event) {
        throw new UnsupportedOperationException();
    }
}
