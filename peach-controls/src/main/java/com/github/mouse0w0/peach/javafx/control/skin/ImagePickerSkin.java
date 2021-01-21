package com.github.mouse0w0.peach.javafx.control.skin;

import com.github.mouse0w0.peach.javafx.control.ImagePicker;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImagePickerSkin extends SkinBase<ImagePicker> {
    private final ImageView imageView;

    private final Tooltip tooltip;

    public ImagePickerSkin(ImagePicker control) {
        super(control);

        consumeMouseEvents(true);

        imageView = new ImageView();
        imageView.fitWidthProperty().bind(control.fitWidthProperty());
        imageView.fitHeightProperty().bind(control.fitHeightProperty());
        imageView.preserveRatioProperty().bind(control.preserveRatioProperty());
        imageView.smoothProperty().bind(control.smoothProperty());
        getChildren().add(imageView);

        tooltip = new Tooltip();
        control.setTooltip(tooltip);

        control.fileProperty().addListener(observable -> updateFile());
        updateFile();
    }

    private void updateFile() {
        File file = getSkinnable().getFile();
        if (file == null) {
            tooltip.setText(null);
            imageView.setImage(null);
        } else {
            tooltip.setText(file.getAbsolutePath());
            try (FileInputStream fis = new FileInputStream(file)) {
                imageView.setImage(new Image(fis, getSkinnable().getFitWidth(), getSkinnable().getFitHeight(), getSkinnable().isPreserveRatio(), getSkinnable().isSmooth()));
            } catch (IOException e) {
                throw new RuntimeException("Failed to load image", e);
            }
        }
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return leftInset + imageView.prefWidth(-1) + rightInset;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + imageView.prefHeight(-1) + bottomInset;
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        layoutInArea(imageView, contentX, contentY, contentWidth, contentHeight, 0, HPos.CENTER, VPos.CENTER);
    }
}
