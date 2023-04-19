package com.github.mouse0w0.peach.action;

import javafx.event.Event;
import javafx.scene.control.ContextMenu;
import org.jetbrains.annotations.NotNull;

public class ActionContextMenu extends ContextMenu implements ActionHolder {
    private final ActionGroup group;

    ActionContextMenu(ActionGroup group) {
        this.group = group;
        setOnShowing(this::update);
        Utils.fillMenu(group, getItems());
    }

    @Override
    public @NotNull ActionGroup getAction() {
        return group;
    }

    private void update(Event event) {
        Utils.update(group, this);
        Utils.updateSeparatorVisibility(getItems());
    }
}
