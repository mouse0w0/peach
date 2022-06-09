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

        Appearance appearance = action.getAppearance();
        textProperty().bind(appearance.textProperty());
        appearance.iconProperty().addListener(new WeakInvalidationListener(iconListener));
        updateIcon();
        disableProperty().bind(appearance.disableProperty());
        visibleProperty().bind(appearance.visibleProperty());

        setOnAction(event -> action.perform(new ActionEvent(event)));
    }

    private void updateIcon() {
        Appearance appearance = action.getAppearance();
        String icon = appearance.getIcon();
        if (icon == null || icon.isEmpty()) {
            setGraphic(null);
        } else {
            setGraphic(IconManager.getInstance().createNode(icon));
        }
    }
}
