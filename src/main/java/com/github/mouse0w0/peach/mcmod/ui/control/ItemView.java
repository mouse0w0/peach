package com.github.mouse0w0.peach.mcmod.ui.control;

import com.github.mouse0w0.peach.mcmod.IdMetadata;
import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.mcmod.ItemData;
import com.github.mouse0w0.peach.mcmod.index.Index;
import com.github.mouse0w0.peach.mcmod.index.IndexKeys;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.project.ModProjectService;
import com.github.mouse0w0.peach.mcmod.tooltip.ItemTooltipProvider;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.project.Project;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.util.Duration;

import java.util.List;

public class ItemView extends Region implements ItemTooltipProvider {
    private final Project project;
    private final Index<IdMetadata, List<ItemData>> index;
    private final ImageView imageView;

    public ItemView(Project project) {
        this.project = project;
        getStyleClass().add("item-view");
        setPickOnBounds(true);

        this.index = IndexManager.getInstance(project).getIndex(IndexKeys.ITEM);
        this.imageView = new ImageView();
        imageView.fitWidthProperty().bind(sizeProperty());
        imageView.fitHeightProperty().bind(sizeProperty());
        getChildren().add(imageView);

        sizeProperty().addListener(observable -> requestLayout());
        itemProperty().addListener(observable -> updateItem());
        playAnimationProperty().addListener(observable -> updateItem());
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

    @Override
    protected double computeMinWidth(double height) {
        return computePrefWidth(height);
    }

    @Override
    protected double computeMinHeight(double width) {
        return computePrefHeight(width);
    }

    @Override
    protected double computePrefWidth(double height) {
        return snappedLeftInset() + snapSizeX(getSize()) + snappedRightInset();
    }

    @Override
    protected double computePrefHeight(double width) {
        return snappedTopInset() + snapSizeY(getSize()) + snappedBottomInset();
    }

    @Override
    protected double computeMaxWidth(double height) {
        return computePrefWidth(height);
    }

    @Override
    protected double computeMaxHeight(double width) {
        return computePrefHeight(width);
    }

    @Override
    protected void layoutChildren() {
        final double x = snappedLeftInset();
        final double y = snappedTopInset();
        final double w = snapSizeX(getWidth()) - x - snappedRightInset();
        final double h = snapSizeY(getHeight()) - y - snappedBottomInset();
        layoutInArea(imageView, x, y, w, h, -1, HPos.CENTER, VPos.CENTER);
    }

    private List<ItemData> itemDataList;
    private int currentItemIndex;
    private Animation animation;

    private void updateItem() {
        if (animation != null) {
            animation.stop();
        }

        IdMetadata item = getItem();
        if (item == null) {
            imageView.setImage(null);
            return;
        }

        itemDataList = index.get(item);

        if (itemDataList == null || itemDataList.isEmpty()) {
            imageView.setImage(ResourceUtils.MISSING_TEXTURE);
            return;
        }

        imageView.setImage(itemDataList.getFirst().getTexture());

        if (isPlayAnimation() && itemDataList.size() > 1) {
            currentItemIndex = 0;
            if (animation == null) {
                animation = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                    currentItemIndex++;
                    if (currentItemIndex >= itemDataList.size()) {
                        currentItemIndex = 0;
                    }
                    imageView.setImage(itemDataList.get(currentItemIndex).getTexture());
                }));
                animation.setCycleCount(Animation.INDEFINITE);
            }
            animation.play();
        }
    }

    public void resetAnimation() {
        if (animation == null) return;
        if (animation.getStatus() != Animation.Status.RUNNING) return;

        currentItemIndex = 0;
        imageView.setImage(itemDataList.getFirst().getTexture());
        animation.jumpTo(Duration.ZERO);
    }

    @Override
    public void addToTooltip(List<String> tooltips) {
        IdMetadata item = getItem();
        if (item == null) return;

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

        if (itemDataList != null) {
            for (ItemData itemData : itemDataList) {
                tooltips.add(itemData.getName());
            }
        }
    }
}
