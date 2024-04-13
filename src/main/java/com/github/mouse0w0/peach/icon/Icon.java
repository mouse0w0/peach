package com.github.mouse0w0.peach.icon;

import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public interface Icon {
    static void setIcon(ObjectProperty<Node> graphic, Icon icon) {
        if (graphic.get() instanceof ImageView imageView) {
            imageView.setImage(icon != null ? icon.getImage() : null);
        } else if (icon != null) {
            graphic.set(new ImageView(icon.getImage()));
        }
    }

    static void setIcon(MenuItem menuItem, Icon icon) {
        if (menuItem.getGraphic() instanceof ImageView imageView) {
            imageView.setImage(icon != null ? icon.getImage() : null);
        } else if (icon != null) {
            menuItem.setGraphic(new ImageView(icon.getImage()));
        }
    }

    static void setIcon(Tab tab, Icon icon) {
        if (tab.getGraphic() instanceof ImageView imageView) {
            imageView.setImage(icon != null ? icon.getImage() : null);
        } else if (icon != null) {
            tab.setGraphic(new ImageView(icon.getImage()));
        }
    }

    static void setIcon(Labeled labeled, Icon icon) {
        if (labeled.getGraphic() instanceof ImageView imageView) {
            imageView.setImage(icon != null ? icon.getImage() : null);
        } else if (icon != null) {
            labeled.setGraphic(new ImageView(icon.getImage()));
        }
    }

    String getName();

    Image getImage();
}
