package com.github.mouse0w0.peach.mcmod.ui.control.skin;

import com.github.mouse0w0.peach.javafx.CachedImage;
import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.ItemRef;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.control.SkinBase;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.List;

public class ItemViewSkin extends SkinBase<ItemView> {

    private static final CachedImage MISSING = new CachedImage("/image/mcmod/missing.png", 64, 64);

    private final ImageView imageView;

    private List<Item> items;
    private Timeline timeline;

    public ItemViewSkin(ItemView itemView) {
        super(itemView);

        consumeMouseEvents(false);

        imageView = new ImageView();
        imageView.fitWidthProperty().bind(itemView.fitWidthProperty());
        imageView.fitHeightProperty().bind(itemView.fitHeightProperty());
        getChildren().add(imageView);

        itemView.itemProperty().addListener(observable -> update());
        itemView.playAnimationProperty().addListener(observable -> update());
        update();
    }

    private void update() {
        ItemView itemView = getSkinnable();

        if (timeline != null) {
            timeline.stop();
        }
        imageView.setImage(null);

        ItemRef itemSelector = itemView.getItem();
        if (itemSelector == null) {
            items = itemView.getItemMap().get(ItemRef.AIR);
        } else {
            items = itemView.getItemMap().get(itemSelector);
        }

        if (items == null || items.size() == 0) {
            imageView.setImage(MISSING.getImage());
            return;
        }

        if (itemView.isPlayAnimation() && items.size() > 1) {
            timeline = createTimeline();
            timeline.play();
        } else {
            imageView.setImage(items.get(0).getDisplayImage().getImage());
        }
    }

    private Timeline createTimeline() {
        Timeline timeline = new Timeline();
        ObservableList<KeyFrame> keyFrames = timeline.getKeyFrames();
        timeline.setCycleCount(Timeline.INDEFINITE);
        for (int i = 0; i < items.size(); i++) {
            CachedImage image = items.get(i).getDisplayImage();
            keyFrames.add(new KeyFrame(Duration.seconds(i), event -> imageView.setImage(image.getImage())));
        }
        return timeline;
    }
}
