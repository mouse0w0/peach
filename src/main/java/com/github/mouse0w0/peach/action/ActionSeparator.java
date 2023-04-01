package com.github.mouse0w0.peach.action;

import javafx.scene.control.SeparatorMenuItem;
import org.jetbrains.annotations.NotNull;

public class ActionSeparator extends SeparatorMenuItem implements ActionHolder {
    private final Separator separator;

    ActionSeparator(Separator separator) {
        this.separator = separator;
    }

    @Override
    public @NotNull Separator getAction() {
        return separator;
    }
}
