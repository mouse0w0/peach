package com.github.mouse0w0.peach.mcmod.ui.control;

import com.github.mouse0w0.peach.mcmod.IdMetadata;
import com.github.mouse0w0.peach.mcmod.ui.control.skin.ItemViewSkin;
import com.github.mouse0w0.peach.project.Project;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.input.*;

public class ItemView extends Control {
    public static final DataFormat ITEM = new DataFormat("peach/item");

    private static final EventHandler<MouseEvent> ON_DRAG_DETECTED = event -> {
        ItemView itemView = (ItemView) event.getSource();
        IdMetadata item = itemView.getItem();
        if (item == null || item.isAir()) return;

        Dragboard dragboard = itemView.startDragAndDrop(TransferMode.LINK);

        ClipboardContent content = new ClipboardContent();
        content.put(ITEM, item);
        dragboard.setContent(content);
    };

    private final Project project;

    public ItemView(Project project) {
        this.project = project;
        getStyleClass().add("item-view");
        setPickOnBounds(true);
        setOnDragDetected(ON_DRAG_DETECTED);
    }

    public ItemView(Project project, double size) {
        this(project);
        setSize(size);
    }

    public ItemView(Project project, IdMetadata item, double size) {
        this(project);
        setItem(item);
        setSize(size);
    }

    public final Project getProject() {
        return project;
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

    public final void resetAnimation() {
        Skin<?> skin = getSkin();
        if (skin != null) {
            ((ItemViewSkin) skin).resetAnimation();
        }
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ItemViewSkin(this);
    }
}
