package com.github.mouse0w0.peach.mcmod.ui.control.skin;

import com.github.mouse0w0.peach.mcmod.IdMetadata;
import com.github.mouse0w0.peach.mcmod.ItemData;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.control.SkinBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.List;

public class ItemViewSkin extends SkinBase<ItemView> {

    private static final Image MISSING = new Image("/image/mcmod/missing.png", 64, 64, true, false);

    private final ImageView imageView;

    private List<ItemData> itemData;
    private Timeline timeline;

    public ItemViewSkin(ItemView itemView) {
        super(itemView);

        consumeMouseEvents(false);

        imageView = new ImageView();
        imageView.fitWidthProperty().bind(itemView.sizeProperty());
        imageView.fitHeightProperty().bind(itemView.sizeProperty());
        getChildren().add(imageView);

        itemView.sizeProperty().addListener(observable -> getSkinnable().requestLayout());
        itemView.itemProperty().addListener(observable -> update());
        itemView.playAnimationProperty().addListener(observable -> update());
        update();
    }

    private void update() {
        ItemView itemView = getSkinnable();

        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }
        imageView.setImage(null);

        IdMetadata itemSelector = itemView.getItem();
        if (itemSelector == null) {
            itemData = itemView.getIndex().get(IdMetadata.AIR);
        } else {
            itemData = itemView.getIndex().get(itemSelector);
        }

        if (itemData == null || itemData.isEmpty()) {
            imageView.setImage(MISSING);
            return;
        }

        if (itemView.isPlayAnimation() && itemData.size() > 1) {
            timeline = createTimeline();
            timeline.play();
        } else {
            imageView.setImage(itemData.get(0).getTexture());
        }
    }

    private Timeline createTimeline() {
        Timeline timeline = new Timeline();
        ObservableList<KeyFrame> keyFrames = timeline.getKeyFrames();
        timeline.setCycleCount(Timeline.INDEFINITE);
        for (int i = 0; i < itemData.size(); i++) {
            Image image = itemData.get(i).getTexture();
            keyFrames.add(new KeyFrame(Duration.seconds(i), event -> imageView.setImage(image)));
        }
        return timeline;
    }

    public void resetAnimation() {
        if (timeline != null) {
            timeline.jumpTo(Duration.ZERO);
        }
    }

    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return leftInset + getSkinnable().getSize() + rightInset;
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + getSkinnable().getSize() + bottomInset;
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return leftInset + getSkinnable().getSize() + rightInset;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + getSkinnable().getSize() + bottomInset;
    }

    @Override
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return leftInset + getSkinnable().getSize() + rightInset;
    }

    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + getSkinnable().getSize() + bottomInset;
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        imageView.resizeRelocate(contentX, contentY, contentWidth, contentHeight);
    }
}
