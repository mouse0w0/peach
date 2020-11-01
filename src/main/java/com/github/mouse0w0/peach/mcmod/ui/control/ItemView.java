package com.github.mouse0w0.peach.mcmod.ui.control;

import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.content.ContentManager;
import com.github.mouse0w0.peach.mcmod.content.data.ItemData;
import com.github.mouse0w0.peach.mcmod.ui.control.skin.ItemViewSkin;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.project.WindowManager;
import javafx.beans.property.*;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.util.List;

public class ItemView extends Control {

    public static final DataFormat ITEM = new DataFormat("peach/item");

    public ItemView() {
        getStyleClass().add("item-view");

        setPickOnBounds(true);

        setOnDragDetected(event -> {
            Item item = getItem();
            if (item == null || item.isAir()) return;

            Dragboard dragboard = startDragAndDrop(TransferMode.LINK);

            ClipboardContent content = new ClipboardContent();
            content.put(ITEM, item);
            dragboard.setContent(content);
        });
        setOnDragOver(event -> {
            event.consume();
            if (event.getGestureSource() == event.getTarget()) return;

            Item item = (Item) event.getDragboard().getContent(ITEM);
            if (item == null) return;

            event.acceptTransferModes(TransferMode.LINK);
        });
        setOnDragDropped(event -> {
            event.consume();
            setItem((Item) event.getDragboard().getContent(ITEM));
            event.setDropCompleted(true);
        });
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

    public ContentManager contentManager;

    public ContentManager getContentManager() {
        if (contentManager == null) {
            Project project = WindowManager.getInstance().getFocusedProject();
            if (project != null) {
                contentManager = ContentManager.getInstance(project);
            }
        }
        return contentManager;
    }

    public void setContentManager(ContentManager contentManager) {
        this.contentManager = contentManager;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ItemViewSkin(this);
    }
}
