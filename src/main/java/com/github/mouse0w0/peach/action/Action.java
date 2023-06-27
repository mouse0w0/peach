package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.icon.Icon;

public abstract class Action {
    private String text;
    private String description;
    private Icon icon;

    public final String getText() {
        return text;
    }

    public final void setText(String text) {
        this.text = text;
    }

    public final String getDescription() {
        return description;
    }

    public final void setDescription(String description) {
        this.description = description;
    }

    public final Icon getIcon() {
        return icon;
    }

    public final void setIcon(Icon icon) {
        this.icon = icon;
    }

    public abstract void perform(ActionEvent event);

    public void update(ActionEvent event) {
        // Nothing to do.
    }
}
