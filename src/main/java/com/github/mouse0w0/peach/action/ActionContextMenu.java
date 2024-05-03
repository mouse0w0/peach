package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.data.DataManager;
import com.github.mouse0w0.peach.data.DataManagerImpl;
import javafx.event.Event;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.stage.Window;
import org.jetbrains.annotations.NotNull;

public final class ActionContextMenu extends ContextMenu implements ActionHolder {
    private final ActionGroup group;
    private final Presentation presentation;

    ActionContextMenu(ActionGroup group) {
        this.group = group;
        this.presentation = new DummyPresentation(group);

        addEventFilter(ActionMenu.ON_SHOWING, this::updateChildren);
    }

    @Override
    public @NotNull ActionGroup getAction() {
        return group;
    }

    @Override
    public void show(Window owner) {
        fillMenu();
        super.show(owner);
    }

    @Override
    public void show(Window ownerWindow, double anchorX, double anchorY) {
        fillMenu();
        super.show(ownerWindow, anchorX, anchorY);
    }

    @Override
    public void show(Node anchor, double screenX, double screenY) {
        fillMenu();
        // Fix JavaFX cannot get owner node of ContextMenu.
        getProperties().put(DataManagerImpl.OWNER_NODE, anchor);
        super.show(anchor, screenX, screenY);
    }

    @Override
    public void show(Node anchor, Side side, double dx, double dy) {
        fillMenu();
        // Fix JavaFX cannot get owner node of ContextMenu.
        getProperties().put(DataManagerImpl.OWNER_NODE, anchor);
        super.show(anchor, side, dx, dy);
    }

    private void fillMenu() {
        getItems().clear();
        Utils.fillMenu(group, new ActionEvent(null, presentation, DataManager.getInstance().getDataContext(this)), getItems());
    }

    private void updateChildren(Event event) {
        Utils.update(getItems());
        Utils.updateSeparatorVisibility(getItems());
    }
}
