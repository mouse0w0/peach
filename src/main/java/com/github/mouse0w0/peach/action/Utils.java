package com.github.mouse0w0.peach.action;

import javafx.collections.ObservableList;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

import java.util.List;

final class Utils {
    public static void fillMenu(ActionGroup parent, ActionEvent event, ObservableList<MenuItem> items) {
        for (Action action : parent.getChildren(event)) {
            switch (action) {
                case null -> throw new NullPointerException("ActionGroup '" +
                        ActionManager.getInstance().getActionId(parent) +
                        "' contains a null child action");
                case ActionGroup group -> {
                    if (group.isPopup()) {
                        items.add(new ActionMenu(group));
                    } else {
                        fillMenu(group, event, items);
                    }
                }
                case ToggleAction toggleAction -> items.add(new ActionToggleMenuItem(toggleAction));
                case Separator separator -> items.add(new ActionSeparatorMenuItem());
                default -> items.add(new ActionMenuItem(action));
            }
        }
    }

    public static void update(List<MenuItem> items) {
        for (MenuItem item : items) {
            if (item instanceof Updatable updatable) {
                updatable.update();
            }
        }
    }

    public static void updateSeparatorVisibility(List<MenuItem> items) {
        boolean perv = false;
        MenuItem separator = null;
        for (MenuItem item : items) {
            if (item instanceof SeparatorMenuItem) {
                if (perv) {
                    perv = false;
                    separator = item;
                } else {
                    item.setVisible(false);
                }
            } else if (item.isVisible()) {
                if (separator != null) {
                    separator.setVisible(true);
                    separator = null;
                }
                perv = true;
            }
        }
        if (separator != null) {
            separator.setVisible(false);
        }
    }

    private Utils() {
    }
}
