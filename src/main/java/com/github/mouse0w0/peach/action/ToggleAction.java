package com.github.mouse0w0.peach.action;

import org.jetbrains.annotations.NotNull;

public abstract class ToggleAction extends Action {
    public abstract boolean isSelected(@NotNull ActionEvent event);

    public abstract void setSelected(@NotNull ActionEvent event, boolean selected);

    @Override
    public void update(ActionEvent event) {
        event.getPresentation().setSelected(isSelected(event));
    }

    @Override
    public void perform(ActionEvent event) {
        boolean newSelected = !isSelected(event);
        setSelected(event, newSelected);
        event.getPresentation().setSelected(newSelected);
    }
}
