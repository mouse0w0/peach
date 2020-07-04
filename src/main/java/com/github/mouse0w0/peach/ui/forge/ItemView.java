package com.github.mouse0w0.peach.ui.forge;

import com.github.mouse0w0.peach.forge.ItemToken;
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

    private final ObjectProperty<ItemToken> itemToken = new SimpleObjectProperty<>(this, "itemSelector");

    private BooleanProperty playAnimation;

    private List<ItemData> itemData;
    private Timeline timeline;

    public ItemView() {
        initialize();
    }

    public ItemView(ItemToken itemToken) {
        setItemToken(itemToken);
        update();
        initialize();
    }

    public ItemView(ItemToken itemToken, double width, double height) {
        setFitSize(width, height);
        setItemToken(itemToken);
        update();
        initialize();
    }

    private void initialize() {
        itemTokenProperty().addListener(observable -> update());
        fitWidthProperty().addListener(observable -> update());
        fitHeightProperty().addListener(observable -> update());
    }

    public final void setFitSize(double width, double height) {
        setFitWidth(width);
        setFitHeight(height);
    }

    public final ObjectProperty<ItemToken> itemTokenProperty() {
        return itemToken;
    }

    public final ItemToken getItemToken() {
        return itemToken.get();
    }

    public final void setItemToken(ItemToken itemToken) {
        this.itemToken.set(itemToken);
    }

    public final List<ItemData> getItemData() {
        return itemData;
    }

    public final BooleanProperty playAnimationProperty() {
        if (playAnimation == null) {
            playAnimation = new SimpleBooleanProperty(this, "playAnimation", true);
            playAnimation.addListener(observable -> update());
        }
        return playAnimation;
    }

    public final boolean isPlayAnimation() {
        return playAnimation == null || playAnimation.get();
    }

    public final void setPlayAnimation(boolean playAnimation) {
        playAnimationProperty().set(playAnimation);
    }

    private void update() {
        itemData = ContentManager.getInstance().getItemData(getItemToken());
        if (timeline != null) {
            timeline.stop();
        }

        if (itemData.size() > 1 && isPlayAnimation()) {
            timeline = new Timeline();
            ObservableList<KeyFrame> keyFrames = timeline.getKeyFrames();
            timeline.setCycleCount(Timeline.INDEFINITE);
            for (int i = 0; i < itemData.size(); i++) {
                ItemData itemDatum = itemData.get(i);
                ImageCache.Key key = new ImageCache.Key(itemDatum.getDisplayImage(), getFitWidth(), getFitHeight());
                keyFrames.add(new KeyFrame(Duration.seconds(i), event -> setImage(ImageCache.getImage(key))));
            }
            timeline.play();
        } else {
            ImageCache.Key key = new ImageCache.Key(itemData.get(0).getDisplayImage(), getFitWidth(), getFitHeight());
            setImage(ImageCache.getImage(key));
        }
    }
}
