package com.github.mouse0w0.peach.mcmod.ui.control;

import com.github.mouse0w0.peach.mcmod.IdMetadata;
import com.github.mouse0w0.peach.mcmod.ItemData;
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
            IdMetadata idMetadata = itemView.getItem();

            StringBuilder sb = new StringBuilder();
            sb.append(idMetadata.getId());
            if (idMetadata.isNormal()) {
                sb.append('#').append(idMetadata.getMetadata());
            }

            sb.append("\n--------------------");

            List<ItemData> itemDataList = itemView.getIndex().get(idMetadata);
            if (itemDataList != null) {
                for (ItemData itemData : itemDataList) {
                    sb.append('\n').append(itemData.getName());
                }
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
            IdMetadata item = getItem();
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

    public ItemView(IdMetadata item, double size) {
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

    private final ObjectProperty<IdMetadata> item = new SimpleObjectProperty<>(this, "item", IdMetadata.AIR);

    public final ObjectProperty<IdMetadata> itemProperty() {
        return item;
    }

    public final IdMetadata getItem() {
        return item.get();
    }

    public final void setItem(IdMetadata value) {
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

    private ObjectProperty<Index<IdMetadata, List<ItemData>>> index;

    public final ObjectProperty<Index<IdMetadata, List<ItemData>>> indexProperty() {
        if (index == null) {
            index = new SimpleObjectProperty<>(this, "index", getDefaultIndex());
        }
        return index;
    }

    public final Index<IdMetadata, List<ItemData>> getIndex() {
        return indexProperty().get();
    }

    public final void setIndex(Index<IdMetadata, List<ItemData>> index) {
        indexProperty().set(index);
    }

    private Index<IdMetadata, List<ItemData>> getDefaultIndex() {
        Project project = WindowManager.getInstance().getFocusedProject();
        if (project == null) return null;
        return IndexManager.getInstance(project).getIndex(IndexTypes.ITEM);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ItemViewSkin(this);
    }
}
