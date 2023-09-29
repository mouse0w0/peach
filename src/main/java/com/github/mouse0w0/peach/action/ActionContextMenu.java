package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.data.DataManagerImpl;
import javafx.event.Event;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.stage.Window;
import org.jetbrains.annotations.NotNull;

public final class ActionContextMenu extends ContextMenu implements ActionHolder {
    private final ActionGroup group;

    private boolean initialized;

    ActionContextMenu(ActionGroup group) {
        this.group = group;
    }

    @Override
    public @NotNull ActionGroup getAction() {
        return group;
    }

    @Override
    public void show(Window owner) {
        initialize();
        super.show(owner);
    }

    @Override
    public void show(Window ownerWindow, double anchorX, double anchorY) {
        initialize();
        super.show(ownerWindow, anchorX, anchorY);
    }

    @Override
    public void show(Node anchor, double screenX, double screenY) {
        initialize();
        // Fix JavaFX cannot get owner node of ContextMenu.
        getProperties().put(DataManagerImpl.OWNER_NODE, anchor);
        super.show(anchor, screenX, screenY);
    }

    @Override
    public void show(Node anchor, Side side, double dx, double dy) {
        initialize();
        // Fix JavaFX cannot get owner node of ContextMenu.
        getProperties().put(DataManagerImpl.OWNER_NODE, anchor);
        super.show(anchor, side, dx, dy);
    }

    private void initialize() {
        if (initialized) return;
        initialized = true;

        setOnShowing(this::update);
        Utils.fillMenu(group, getItems());
    }

    private void update(Event event) {
        Utils.update(event, getItems());
        Utils.updateSeparatorVisibility(getItems());
    }
}
