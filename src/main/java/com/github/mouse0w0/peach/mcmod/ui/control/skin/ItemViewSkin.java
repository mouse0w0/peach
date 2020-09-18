package com.github.mouse0w0.peach.mcmod.ui.control.skin;

import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.content.ContentManager;
import com.github.mouse0w0.peach.mcmod.content.data.ItemData;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import com.github.mouse0w0.peach.ui.project.WindowManager;
import com.github.mouse0w0.peach.ui.util.CachedImage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.control.SkinBase;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Collections;
import java.util.List;

public class ItemViewSkin extends SkinBase<ItemView> {

    private final ImageView imageView;

    private List<ItemData> itemData;
    private Timeline timeline;

    public ItemViewSkin(ItemView itemView) {
        super(itemView);

        consumeMouseEvents(false);

        imageView = new ImageView();
        imageView.fitWidthProperty().bind(itemView.fitWidthProperty());
        imageView.fitHeightProperty().bind(itemView.fitHeightProperty());
        getChildren().add(imageView);

        itemView.fitWidthProperty().addListener(observable -> update());
        itemView.fitHeightProperty().addListener(observable -> update());
        itemView.itemProperty().addListener(observable -> update());
        itemView.playAnimationProperty().addListener(observable -> update());
        update();
    }

    public List<ItemData> getItemData() {
        return itemData;
    }

    private Item getItem() {
        return getSkinnable().getItem();
    }

    private double getFitWidth() {
        return getSkinnable().getFitWidth();
    }

    private double getFitHeight() {
        return getSkinnable().getFitHeight();
    }

    private boolean isPlayAnimation() {
        return getSkinnable().isPlayAnimation();
    }

    private void update() {
        if (timeline != null) {
            timeline.stop();
        }
        imageView.setImage(null);

        Item item = getItem();
        if (item == null) {
            itemData = Collections.emptyList();
        } else {
            itemData = ContentManager.getInstance(WindowManager.getInstance().getFocusedWindow().getProject()).getItemData(item);
        }

        if (itemData.size() == 0) {
            return;
        }

        if (isPlayAnimation() && itemData.size() > 1) {
            timeline = createTimeline();
            timeline.play();
        } else {
            ItemData itemDatum = itemData.get(0);
            CachedImage image = new CachedImage(itemDatum.getDisplayImage(), getFitWidth(), getFitHeight());
            imageView.setImage(image.getImage());
        }
    }

    private Timeline createTimeline() {
        Timeline timeline = new Timeline();
        ObservableList<KeyFrame> keyFrames = timeline.getKeyFrames();
        timeline.setCycleCount(Timeline.INDEFINITE);
        for (int i = 0; i < itemData.size(); i++) {
            ItemData itemDatum = itemData.get(i);
            CachedImage image = new CachedImage(itemDatum.getDisplayImage(), getFitWidth(), getFitHeight());
            keyFrames.add(new KeyFrame(Duration.seconds(i), event -> imageView.setImage(image.getImage())));
        }
        return timeline;
    }
}
