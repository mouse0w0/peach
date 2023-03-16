package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.util.Validate;
import javafx.event.Event;
import javafx.scene.control.ContextMenu;

public class ActionContextMenu extends ContextMenu {
    private final ActionGroup group;

    public ActionContextMenu(ActionGroup group) {
        this.group = Validate.notNull(group);
        getProperties().put(Action.class, group);
        setOnShowing(this::update);
        Utils.fillMenu(group, getItems());
    }

    public ActionGroup getGroup() {
        return group;
    }

    private void update(Event event) {
        Utils.update(group, event);
        Utils.updateSeparatorVisibility(getItems());
    }
}
