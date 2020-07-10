package com.github.mouse0w0.peach.forge.ui;

import com.github.mouse0w0.peach.forge.Item;
import com.github.mouse0w0.peach.forge.contentPack.ContentManager;
import com.github.mouse0w0.peach.forge.contentPack.data.ItemData;
import com.github.mouse0w0.peach.ui.util.ImageCache;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.List;

public class ItemView extends ImageView {

    private final ObjectProperty<Item> item = new SimpleObjectProperty<>(this, "itemSelector");

    private BooleanProperty playAnimation;

    private List<ItemData> itemData;
    private Timeline timeline;

    public ItemView() {
        initialize();
    }

    public ItemView(Item item) {
        setItem(item);
        update();
        initialize();
    }

    public ItemView(Item item, double width, double height) {
        setFitSize(width, height);
        setItem(item);
        update();
        initialize();
    }

    private void initialize() {
        itemProperty().addListener(observable -> update());
        fitWidthProperty().addListener(observable -> update());
        fitHeightProperty().addListener(observable -> update());
    }

    public final void setFitSize(double width, double height) {
        setFitWidth(width);
        setFitHeight(height);
    }

    public final ObjectProperty<Item> itemProperty() {
        return item;
    }

    public final Item getItem() {
        return item.get();
    }

    public final void setItem(Item item) {
        this.item.set(item);
    }

    public final List<ItemData> getItemData() {
        return itemData;
    }

    public final BooleanProperty playAnimationProperty() {
        if (playAnimation == null) {
            playAnimation = new SimpleBooleanProperty(this, "playAnimation", false);
            playAnimation.addListener(observable -> update());
        }
        return playAnimation;
    }

    public final boolean isPlayAnimation() {
        return playAnimation != null && playAnimation.get();
    }

    public final void setPlayAnimation(boolean playAnimation) {
        playAnimationProperty().set(playAnimation);
    }

    private void update() {
        if (timeline != null) {
            timeline.stop();
        }

        itemData = ContentManager.getInstance().getItemData(getItem());
        if (itemData.size() == 0) {
            setImage(null);
            return;
        }

        if (itemData.size() > 1 && isPlayAnimation()) {
            timeline = createTimeline();
            timeline.play();
        } else {
            ImageCache.Key key = new ImageCache.Key(itemData.get(0).getDisplayImage(), getFitWidth(), getFitHeight());
            setImage(ImageCache.getImage(key));
        }
    }

    private Timeline createTimeline() {
        Timeline timeline = new Timeline();
        ObservableList<KeyFrame> keyFrames = timeline.getKeyFrames();
        timeline.setCycleCount(Timeline.INDEFINITE);
        for (int i = 0; i < itemData.size(); i++) {
            ItemData itemDatum = itemData.get(i);
            ImageCache.Key key = new ImageCache.Key(itemDatum.getDisplayImage(), getFitWidth(), getFitHeight());
            keyFrames.add(new KeyFrame(Duration.seconds(i), event -> setImage(ImageCache.getImage(key))));
        }
        return timeline;
    }
}
