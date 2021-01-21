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

    public ActionMenu(ActionGroup group) {
        this.group = group;

        getProperties().put(Action.class, group);

        Appearance appearance = group.getAppearance();
        textProperty().bind(appearance.textProperty());
        appearance.iconProperty().addListener(new WeakInvalidationListener(iconListener));
        updateIcon();
        disableProperty().bind(appearance.disableProperty());
        visibleProperty().bind(appearance.visibleProperty());

        addEventHandler(ON_SHOWING, this::update);
    }

    public ActionGroup getGroup() {
        return group;
    }

    private void update(Event event) {
        group.update(new ActionEvent(event.getSource()));

        for (MenuItem item : getItems()) {
            Action action = (Action) item.getProperties().get(Action.class);
            action.update(new ActionEvent(item));
        }
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
