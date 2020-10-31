package com.github.mouse0w0.peach.ui.icon;

import javafx.scene.image.Image;

public interface Icons {

    Image Peach_16x = load("/icon/peach-16x.png");

    interface Action {
        Image NewProject = load("/icon/plus-thick.png");

        Image OpenProject = load("/icon/folder-open-outline.png");

        Image ProjectSettings = load("/icon/tune.png");

        Image Export = load("/icon/export.png");

        Image Donate = load("/icon/currency-usd-circle-outline.png");
    }

    static Image load(String name) {
        return new Image(Icons.class.getResource(name).toExternalForm());
    }
}
