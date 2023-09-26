package com.github.mouse0w0.peach.action;

import javafx.scene.control.SeparatorMenuItem;
import org.jetbrains.annotations.NotNull;

public final class ActionSeparator extends SeparatorMenuItem implements ActionHolder {
    ActionSeparator() {
    }

    @Override
    public @NotNull Separator getAction() {
        return Separator.getInstance();
    }
}
