package com.github.mouse0w0.peach.action;

import javafx.scene.control.SeparatorMenuItem;
import org.jetbrains.annotations.NotNull;

public final class ActionSeparatorMenuItem extends SeparatorMenuItem implements ActionHolder {
    ActionSeparatorMenuItem() {
    }

    @Override
    public @NotNull Separator getAction() {
        return Separator.getInstance();
    }
}
