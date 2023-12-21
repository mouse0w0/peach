package com.github.mouse0w0.peach.mcmod.ui.control;

import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.ItemRef;
import com.github.mouse0w0.peach.mcmod.index.Index;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.index.IndexTypes;
import com.github.mouse0w0.peach.mcmod.ui.control.skin.ItemViewSkin;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.window.WindowManager;
import javafx.beans.property.*;
import javafx.css.Styleable;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.util.List;

public class ItemView extends Control {
    private static final Tooltip TOOLTIP = createTooltip();

    public static final DataFormat ITEM = new DataFormat("peach/item");

    private static Tooltip createTooltip() {
        Tooltip tooltip = new Tooltip();
        tooltip.setOnShowing(event -> {
            Styleable parent = tooltip.getStyleableParent();
            if (parent == null) return;

            ItemView itemView = (ItemView) parent;
            ItemRef item = itemView.getItem();

            StringBuilder sb = new StringBuilder();
            sb.append(item.getId());
            if (item.isNormal()) sb.append('#').append(item.getMetadata());

            sb.append("\n--------------------");

            for (Item data : itemView.getIndex().get(item)) {
                sb.append('\n').append(data.getLocalizedText());
            }

            tooltip.setText(sb.toString());
        });
        return tooltip;
    }

    public ItemView() {
        getStyleClass().add("item-view");

        setPickOnBounds(true);
        setTooltip(TOOLTIP);

        setOnDragDetected(event -> {
            ItemRef item = getItem();
            if (item == null || item.isAir()) return;

            Dragboard dragboard = startDragAndDrop(TransferMode.LINK);

            ClipboardContent content = new ClipboardContent();
            content.put(ITEM, item);
            dragboard.setContent(content);
        });
    }

    public ItemView(double size) {
        this();
        setSize(size);
    }

    public ItemView(ItemRef item, double size) {
        this();
        setItem(item);
        setSize(size);
    }

    private final DoubleProperty size = new SimpleDoubleProperty(this, "size");

    public final DoubleProperty sizeProperty() {
        return size;
    }

    public final double getSize() {
        return size.get();
    }

    public final void setSize(double value) {
        size.set(value);
    }

    private final ObjectProperty<ItemRef> item = new SimpleObjectProperty<>(this, "item", ItemRef.AIR);

    public final ObjectProperty<ItemRef> itemProperty() {
        return item;
    }

    public final ItemRef getItem() {
        return item.get();
    }

    public final void setItem(ItemRef value) {
        item.set(value);
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

    private ObjectProperty<Index<ItemRef, List<Item>>> index;

    public final ObjectProperty<Index<ItemRef, List<Item>>> indexProperty() {
        if (index == null) {
            index = new SimpleObjectProperty<>(this, "index", getDefaultIndex());
        }
        return index;
    }

    public final Index<ItemRef, List<Item>> getIndex() {
        return indexProperty().get();
    }

    public final void setIndex(Index<ItemRef, List<Item>> index) {
        indexProperty().set(index);
    }

    private Index<ItemRef, List<Item>> getDefaultIndex() {
        Project project = WindowManager.getInstance().getFocusedProject();
        if (project == null) return null;
        return IndexManager.getInstance(project).getIndex(IndexTypes.ITEMS);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ItemViewSkin(this);
    }
}
