package com.github.mouse0w0.peach.mcmod.ui;

import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.content.ContentManager;
import com.github.mouse0w0.peach.mcmod.contentPack.data.ItemData;
import com.github.mouse0w0.peach.ui.project.WindowManager;
import com.github.mouse0w0.peach.ui.util.CachedImage;
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
        this(null, 64, 64);
    }

    public ItemView(double width, double height) {
        this(null, width, height);
    }

    public ItemView(Item item) {
        this(item, 64, 64);
    }

    public ItemView(Item item, double width, double height) {
        setItem(item);
        setFitSize(width, height);
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
        setImage(null);

        Item item = getItem();
        if (item == null) return;

        itemData = ContentManager.getInstance(
                WindowManager.getInstance().getFocusedWindow().getProject()).getItemData(item);
        if (itemData.size() == 0) return;

        if (isPlayAnimation() && itemData.size() > 1) {
            timeline = createTimeline();
            timeline.play();
        } else {
            ItemData itemDatum = itemData.get(0);
            CachedImage image = new CachedImage(itemDatum.getDisplayImage(), getFitWidth(), getFitHeight());
            setImage(image.getImage());
        }
    }

    private Timeline createTimeline() {
        Timeline timeline = new Timeline();
        ObservableList<KeyFrame> keyFrames = timeline.getKeyFrames();
        timeline.setCycleCount(Timeline.INDEFINITE);
        for (int i = 0; i < itemData.size(); i++) {
            ItemData itemDatum = itemData.get(i);
            CachedImage image = new CachedImage(itemDatum.getDisplayImage(), getFitWidth(), getFitHeight());
            keyFrames.add(new KeyFrame(Duration.seconds(i), event -> setImage(image.getImage())));
        }
        return timeline;
    }
}
