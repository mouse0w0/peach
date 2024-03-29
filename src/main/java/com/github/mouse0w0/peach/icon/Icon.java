package com.github.mouse0w0.peach.icon;

import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public interface Icon {
    static void apply(ObjectProperty<Node> graphic, Icon icon) {
        if (graphic.get() instanceof ImageView imageView) {
            imageView.setImage(icon != null ? icon.getImage() : null);
        } else if (icon != null) {
            graphic.set(new ImageView(icon.getImage()));
        }
    }

    String getName();

    Image getImage();
}
