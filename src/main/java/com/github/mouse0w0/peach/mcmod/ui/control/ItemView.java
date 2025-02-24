package com.github.mouse0w0.peach.mcmod.ui.control;

import com.github.mouse0w0.peach.mcmod.IdMetadata;
import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.mcmod.ItemData;
import com.github.mouse0w0.peach.mcmod.project.ModProjectService;
import com.github.mouse0w0.peach.mcmod.tooltip.ItemTooltipProvider;
import com.github.mouse0w0.peach.mcmod.ui.control.skin.ItemViewSkin;
import com.github.mouse0w0.peach.project.Project;
import javafx.beans.property.*;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import java.util.List;

public class ItemView extends Control implements ItemTooltipProvider {
    private final Project project;

    public ItemView(Project project) {
        this.project = project;
        getStyleClass().add("item-view");
        setPickOnBounds(true);
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

    private final ObjectProperty<IdMetadata> item = new SimpleObjectProperty<>(this, "item", IdMetadata.air());

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

    private ItemViewSkin getItemViewSkin() {
        return (ItemViewSkin) getSkin();
    }

    @Override
    public void addToTooltip(List<String> tooltips) {
        IdMetadata item = getItem();
        Identifier id = item.getIdentifier();

        StringBuilder sb = new StringBuilder();
        if (!item.isOreDictionary()) {
            if (id.isProjectNamespace()) {
                sb.append(ModProjectService.getInstance(project).getModId());
            } else {
                sb.append(id.getNamespace());
            }
            sb.append(':');
        }
        sb.append(id.getPath());
        if (item.isNormal()) {
            sb.append('#').append(item.getMetadata());
        }

        tooltips.add(sb.toString());
        tooltips.add("--------------------");

        List<ItemData> itemDataList = getItemViewSkin().getIndex().get(item);
        if (itemDataList != null) {
            for (ItemData itemData : itemDataList) {
                tooltips.add(itemData.getName());
            }
        }
    }
}
