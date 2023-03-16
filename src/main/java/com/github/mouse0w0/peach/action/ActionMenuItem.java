package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.icon.IconManager;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.scene.control.MenuItem;

public class ActionMenuItem extends MenuItem {
    private final Action action;

    private final InvalidationListener iconListener = observable -> updateIcon();

    public ActionMenuItem(Action action) {
        this.action = action;

        getProperties().put(Action.class, action);

        textProperty().bind(action.textProperty());
        action.iconProperty().addListener(new WeakInvalidationListener(iconListener));
        updateIcon();
        disableProperty().bind(action.disableProperty());
        visibleProperty().bind(action.visibleProperty());

        setOnAction(event -> action.perform(new ActionEvent(event)));
    }

    private void updateIcon() {
        String icon = action.getIcon();
        if (icon == null || icon.isEmpty()) {
            setGraphic(null);
        } else {
            setGraphic(IconManager.getInstance().createNode(icon));
        }
    }
}
