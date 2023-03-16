package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.Peach;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;

import javax.annotation.Nonnull;

public interface ActionManager {

    static ActionManager getInstance() {
        return Peach.getInstance().getService(ActionManager.class);
    }

    String getActionId(Action action);

    Action getAction(String actionId);

    void perform(String actionId, Event event);

    Menu createMenu(@Nonnull ActionGroup group);

    ContextMenu createContextMenu(@Nonnull ActionGroup group);

    Button createButton(@Nonnull Action action);
}
