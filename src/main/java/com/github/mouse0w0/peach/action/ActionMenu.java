package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.icon.IconManager;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.event.Event;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class ActionMenu extends Menu {
    private final ActionGroup group;

    private final InvalidationListener iconListener = observable -> updateIcon();

    private boolean initialized;

    public ActionMenu(ActionGroup group) {
        this.group = group;

        getProperties().put(Action.class, group);
        setOnShowing(this::update);

        Appearance appearance = group.getAppearance();
        textProperty().bind(appearance.textProperty());
        appearance.iconProperty().addListener(new WeakInvalidationListener(iconListener));
        updateIcon();
        disableProperty().bind(appearance.disableProperty());
        visibleProperty().bind(appearance.visibleProperty());

        // Fix JavaFX don't show empty menu.
        if (group.getChildren().isEmpty()) {
            initialized = true;
        } else {
            getItems().add(new MenuItem());
        }
    }

    public ActionGroup getGroup() {
        return group;
    }

    @Override
    public void show() {
        initialize();
        super.show();
    }

    private void initialize() {
        if (!initialized) {
            initialized = true;
            Utils.fillMenu(group, getItems());
        }
    }

    private void update(Event event) {
        Utils.update(group, this);
        Utils.updateSeparatorVisibility(getItems());
    }

    private void updateIcon() {
        Appearance appearance = group.getAppearance();
        String icon = appearance.getIcon();
        if (icon == null || icon.isEmpty()) {
            setGraphic(null);
        } else {
            setGraphic(IconManager.getInstance().createNode(icon));
        }
    }
}
