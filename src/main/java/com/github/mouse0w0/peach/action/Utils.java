package com.github.mouse0w0.peach.action;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class Utils {
    static void fillMenu(ActionGroup group, ObservableList<MenuItem> items) {
        items.clear();
        for (Action action : group.getChildren()) {
            if (action instanceof ActionGroup) {
                items.add(new ActionMenu((ActionGroup) action));
            } else if (action instanceof Separator) {
                items.add(new ActionSeparator(action));
            } else {
                items.add(new ActionMenuItem(action));
            }
        }
    }

    static void update(ActionGroup group, Event event) {
        final ActionEvent actionEvent = new ActionEvent(event);
        for (Action child : group.getChildren()) {
            child.update(actionEvent);
        }
    }

    static void updateSeparatorVisibility(ObservableList<MenuItem> items) {
        boolean perv = false;
        MenuItem seperator = null;
        for (MenuItem item : items) {
            if (item instanceof SeparatorMenuItem) {
                if (perv) {
                    perv = false;
                    seperator = item;
                } else {
                    item.setVisible(false);
                }
            } else if (item.isVisible()) {
                if (seperator != null) {
                    seperator.setVisible(true);
                    seperator = null;
                }
                perv = true;
            }
        }
        if (seperator != null) {
            seperator.setVisible(false);
        }
    }
}
