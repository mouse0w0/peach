package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.data.DataManagerImpl;
import javafx.event.Event;
import javafx.scene.Node;
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

    @Override
    public void show(Node anchor, double screenX, double screenY) {
        // Fix JavaFX cannot get owner node of ContextMenu.
        getProperties().put(DataManagerImpl.OWNER_NODE, anchor);
        super.show(anchor, screenX, screenY);
    }

    private void update(Event event) {
        Utils.update(event, getItems());
        Utils.updateSeparatorVisibility(getItems());
    }
}
