package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.icon.Icon;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

class Utils {
    public static void fillMenu(ActionGroup parent, ObservableList<MenuItem> items) {
        for (Action action : parent.getChildren()) {
            if (action instanceof ActionGroup group) {
                if (group.isPopup()) {
                    items.add(new ActionMenu(group));
                } else {
                    fillMenu(group, items);
                }
            } else if (action instanceof Separator) {
                items.add(new ActionSeparator((Separator) action));
            } else {
                items.add(new ActionMenuItem(action));
            }
        }
    }

    public static void update(Event event, List<MenuItem> items) {
        for (MenuItem item : items) {
            if (item instanceof ActionMenuItem) {
                ((ActionMenuItem) item).update(event);
            } else if (item instanceof ActionMenu) {
                ((ActionMenu) item).update(event);
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

    public static void setIcon(ObjectProperty<Node> graphicProperty, @Nullable Icon icon) {
        graphicProperty.set(icon != null ? new ImageView(icon.getImage()) : null);
    }

    private Utils() {
    }
}
