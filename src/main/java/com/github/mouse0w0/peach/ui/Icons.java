package com.github.mouse0w0.peach.ui;

import javafx.scene.image.Image;

public interface Icons {

    Image PEACH_16X = load("icon/peach-16x");

    static Image load(String resourceName) {
        return new Image(Icons.class.getClassLoader().getResource(resourceName + ".png").toExternalForm());
    }
}
