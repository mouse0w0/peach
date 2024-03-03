package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.Peach;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ActionManager {
    static ActionManager getInstance() {
        return Peach.getInstance().getService(ActionManager.class);
    }

    @Nullable
    String getActionId(@NotNull Action action);

    Action getAction(@NotNull String actionId);

    void perform(@NotNull String actionId, @NotNull Object source);

    @NotNull
    Menu createMenu(@NotNull ActionGroup group);

    @NotNull
    ContextMenu createContextMenu(@NotNull ActionGroup group);

    @NotNull
    Button createButton(@NotNull Action action);
}
