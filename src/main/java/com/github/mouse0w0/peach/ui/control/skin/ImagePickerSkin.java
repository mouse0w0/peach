package com.github.mouse0w0.peach.ui.control.skin;

import com.github.mouse0w0.peach.ui.control.ImagePicker;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.SkinBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImagePickerSkin extends SkinBase<ImagePicker> {
    private final ImageView imageView;
    private final StackPane imageOuter;
    private final StackPane add;
    private final StackPane clearBtn;

    public ImagePickerSkin(ImagePicker control) {
        super(control);

        imageView = new ImageView();
        imageView.fitWidthProperty().bind(control.fitWidthProperty());
        imageView.fitHeightProperty().bind(control.fitHeightProperty());
        imageView.preserveRatioProperty().bind(control.preserveRatioProperty());
        imageView.smoothProperty().bind(control.smoothProperty());

        imageOuter = new StackPane(imageView);
        imageOuter.getStyleClass().add("image-outer");

        add = new StackPane();
        add.getStyleClass().add("add");
        add.visibleProperty().bind(control.fileProperty().isNull());

        clearBtn = new StackPane();
        clearBtn.getStyleClass().add("clear-button");
        clearBtn.setOnMouseClicked(event -> {
            event.consume();
            control.setFile(null);
        });
        clearBtn.visibleProperty().bind(control.hoverProperty().and(control.fileProperty().isNotNull()));

        StackPane clear = new StackPane();
        clear.getStyleClass().add("clear");
        clearBtn.getChildren().add(clear);

        getChildren().addAll(imageOuter, add, clearBtn);

        control.fileProperty().addListener(observable -> updateFile());
        updateFile();
    }

    private void updateFile() {
        ImagePicker skinnable = getSkinnable();
        File file = skinnable.getFile();
        if (file == null) {
            imageView.setImage(null);
        } else {
            try (FileInputStream fis = new FileInputStream(file)) {
                imageView.setImage(new Image(fis, skinnable.getFitWidth(), skinnable.getFitHeight(), skinnable.isPreserveRatio(), skinnable.isSmooth()));
            } catch (IOException e) {
                throw new RuntimeException("Failed to load image", e);
            }
        }
    }

    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return leftInset + snapSizeX(imageOuter.minWidth(-1)) + rightInset;
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + snapSizeY(imageOuter.minHeight(-1)) + bottomInset;
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return leftInset + snapSizeX(imageOuter.prefWidth(-1)) + rightInset;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + snapSizeY(imageOuter.prefHeight(-1)) + bottomInset;
    }

    @Override
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return leftInset + snapSizeX(imageOuter.maxWidth(-1)) + rightInset;
    }

    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + snapSizeY(imageOuter.maxHeight(-1)) + bottomInset;
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        layoutInArea(imageOuter, contentX, contentY, contentWidth, contentHeight, 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(add, contentX, contentY, contentWidth, contentHeight, 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(clearBtn, contentX, contentY, contentWidth, contentHeight, 0, HPos.RIGHT, VPos.TOP);
    }
}
