package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.icon.Icon;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

class Utils {
    public static void fillMenu(ActionGroup parent, ActionEvent event, ObservableList<MenuItem> items) {
        for (Action action : parent.getChildren(event)) {
            if (action instanceof ActionGroup group) {
                if (group.isPopup()) {
                    items.add(new ActionMenu(group));
                } else {
                    fillMenu(group, event, items);
                }
            } else if (action instanceof ToggleAction toggleAction) {
                items.add(new ActionToggleMenuItem(toggleAction));
            } else if (action instanceof Separator) {
                items.add(new ActionSeparatorMenuItem());
            } else {
                items.add(new ActionMenuItem(action));
            }
        }
    }

    public static void update(List<MenuItem> items) {
        for (MenuItem item : items) {
            if (item instanceof ActionMenuItem) {
                ((ActionMenuItem) item).update();
            } else if (item instanceof ActionToggleMenuItem) {
                ((ActionToggleMenuItem) item).update();
            } else if (item instanceof ActionMenu) {
                ((ActionMenu) item).update();
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
        Node graphic = graphicProperty.get();
        if (graphic instanceof ImageView imageView) {
            imageView.setImage(icon != null ? icon.getImage() : null);
        } else if (icon != null) {
            graphicProperty.set(new ImageView(icon.getImage()));
        }
    }

    private Utils() {
    }
}
