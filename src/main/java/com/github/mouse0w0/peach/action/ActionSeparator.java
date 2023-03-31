package com.github.mouse0w0.peach.action;

import javafx.scene.control.SeparatorMenuItem;

public class ActionSeparator extends SeparatorMenuItem {
    private final Separator separator;

    ActionSeparator(Separator separator) {
        this.separator = separator;
    }

    public Separator getSeparator() {
        return separator;
    }
}
