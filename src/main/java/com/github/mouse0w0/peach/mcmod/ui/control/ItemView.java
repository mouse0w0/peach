package com.github.mouse0w0.peach.mcmod.ui.control;

import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.content.data.ItemData;
import com.github.mouse0w0.peach.mcmod.ui.control.skin.ItemViewSkin;
import javafx.beans.property.*;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import java.util.List;

public class ItemView extends Control {

    public ItemView() {
        getStyleClass().add("item-view");

        setPickOnBounds(true);
    }

    public ItemView(double width, double height) {
        this();
        setFitSize(width, height);
    }

    public ItemView(Item item) {
        this();
        setItem(item);
    }

    public ItemView(Item item, double width, double height) {
        this();
        setItem(item);
        setFitSize(width, height);
    }

    private DoubleProperty fitWidth;

    public final void setFitWidth(double value) {
        fitWidthProperty().set(value);
    }

    public final double getFitWidth() {
        return fitWidth == null ? 0 : fitWidth.get();
    }

    public final DoubleProperty fitWidthProperty() {
        if (fitWidth == null) {
            fitWidth = new SimpleDoubleProperty(this, "fitWidth");
        }
        return fitWidth;
    }

    private DoubleProperty fitHeight;

    public final void setFitHeight(double value) {
        fitHeightProperty().set(value);
    }

    public final double getFitHeight() {
        return fitHeight == null ? 0 : fitHeight.get();
    }

    public final DoubleProperty fitHeightProperty() {
        if (fitHeight == null) {
            fitHeight = new SimpleDoubleProperty(this, "fitHeight");
        }
        return fitHeight;
    }

    public final void setFitSize(double width, double height) {
        setFitWidth(width);
        setFitHeight(height);
    }

    private final ObjectProperty<Item> item = new SimpleObjectProperty<>(this, "item");

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
        return ((ItemViewSkin) getSkin()).getItemData();
    }

    private BooleanProperty playAnimation;

    public final BooleanProperty playAnimationProperty() {
        if (playAnimation == null) {
            playAnimation = new SimpleBooleanProperty(this, "playAnimation", false);
        }
        return playAnimation;
    }

    public final boolean isPlayAnimation() {
        return playAnimation != null && playAnimation.get();
    }

    public final void setPlayAnimation(boolean playAnimation) {
        playAnimationProperty().set(playAnimation);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ItemViewSkin(this);
    }
}
