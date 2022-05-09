package com.github.mouse0w0.peach.action;

import javafx.collections.ObservableList;
import javafx.scene.control.MenuItem;

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
}
