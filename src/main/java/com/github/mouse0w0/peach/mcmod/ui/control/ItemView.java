package com.github.mouse0w0.peach.mcmod.ui.control;

import com.github.mouse0w0.peach.javafx.FXUtils;
import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.ItemRef;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.index.StandardIndexes;
import com.github.mouse0w0.peach.mcmod.ui.control.skin.ItemViewSkin;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.wm.WindowManager;
import javafx.beans.property.*;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.util.List;
import java.util.Map;

public class ItemView extends Control {
    private static final Tooltip TOOLTIP = createTooltip();

    public static final DataFormat ITEM = new DataFormat("peach/item");

    private static Tooltip createTooltip() {
        Tooltip tooltip = new Tooltip();
        tooltip.setOnShowing(event ->
                FXUtils.getTooltipOwnerNode().ifPresent(node -> {
                            ItemView itemView = (ItemView) node;
                            ItemRef item = itemView.getItem();
                            if (!itemView.isEnableTooltip() || item == null) return;

                            StringBuilder sb = new StringBuilder();

                            sb.append(item.getId());
                            if (item.isNormal()) sb.append("#").append(item.getMetadata());

                            sb.append("\n--------------------\n");

                    for (Item data : itemView.getItemMap().get(item)) {
                        sb.append(data.getDisplayName()).append("\n");
                    }

                            tooltip.setText(sb.substring(0, sb.length() - 1));
                        }
                ));
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

    public ItemView(double width, double height) {
        this(ItemRef.AIR, width, height);
    }

    public ItemView(ItemRef item, double width, double height) {
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

    private final ObjectProperty<ItemRef> item = new SimpleObjectProperty<>(this, "item", ItemRef.AIR);

    public final ObjectProperty<ItemRef> itemProperty() {
        return item;
    }

    public final ItemRef getItem() {
        return item.get();
    }

    public final void setItem(ItemRef item) {
        this.item.set(item);
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

    private BooleanProperty enableTooltip;

    public BooleanProperty enableTooltipProperty() {
        if (enableTooltip == null) {
            enableTooltip = new SimpleBooleanProperty(this, "enableTooltip", true);
        }
        return enableTooltip;
    }

    public boolean isEnableTooltip() {
        return enableTooltip == null || enableTooltip.get();
    }

    public void setEnableTooltip(boolean enableTooltip) {
        enableTooltipProperty().set(enableTooltip);
    }

    private ObjectProperty<Map<ItemRef, List<Item>>> itemMap;

    public final ObjectProperty<Map<ItemRef, List<Item>>> itemMapProperty() {
        if (itemMap == null) {
            itemMap = new SimpleObjectProperty<>(this, "itemMap", getDefaultItemMap());
        }
        return itemMap;
    }

    public final Map<ItemRef, List<Item>> getItemMap() {
        return itemMap == null ? getDefaultItemMap() : itemMap.get();
    }

    public final void setItemMap(Map<ItemRef, List<Item>> itemMap) {
        itemMapProperty().set(itemMap);
    }

    private Map<ItemRef, List<Item>> getDefaultItemMap() {
        Project project = WindowManager.getInstance().getFocusedProject();
        if (project == null) return null;
        return IndexManager.getInstance(project).getIndex(StandardIndexes.ITEMS);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ItemViewSkin(this);
    }
}
