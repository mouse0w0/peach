package com.github.mouse0w0.peach.action;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ActionMenuItem extends MenuItem {
    private final Action action;

    private final InvalidationListener iconListener = observable -> updateIcon();

    private ImageView imageView;

    public ActionMenuItem(Action action) {
        this.action = action;
        Appearance appearance = action.getAppearance();
        textProperty().bind(appearance.textProperty());

        appearance.iconProperty().addListener(new WeakInvalidationListener(iconListener));
        updateIcon();

        disableProperty().bind(appearance.disableProperty());
        visibleProperty().bind(appearance.visibleProperty());

        setOnAction(event -> action.perform(new ActionEvent(event.getSource())));
    }

    private void updateIcon() {
        Appearance appearance = action.getAppearance();
        String icon = appearance.getIcon();
        if (icon == null || icon.isEmpty()) {
            setGraphic(null);
        } else {
            if (imageView == null) {
                imageView = new ImageView();
            }
            imageView.setImage(new Image(icon, true));
            setGraphic(imageView);
        }
    }
}
