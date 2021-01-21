package com.github.mouse0w0.peach.mcmod.ui.control.skin;

import com.github.mouse0w0.peach.javafx.CachedImage;
import com.github.mouse0w0.peach.javafx.FXUtils;
import com.github.mouse0w0.peach.mcmod.ItemRef;
import com.github.mouse0w0.peach.mcmod.content.data.ItemData;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.List;

public class ItemViewSkin extends SkinBase<ItemView> {

    private static final CachedImage MISSING = new CachedImage("/image/mcmod/missing.png", 64, 64);

    private static final Tooltip TOOLTIP = createTooltip();

    private final ImageView imageView;

    private List<ItemData> itemData;
    private Timeline timeline;

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

                            for (ItemData data : itemView.getItemMap().get(item)) {
                                sb.append(data.getDisplayName()).append("\n");
                            }

                            tooltip.setText(sb.substring(0, sb.length() - 1));
                        }
                ));
        return tooltip;
    }

    public ItemViewSkin(ItemView itemView) {
        super(itemView);

        getSkinnable().setTooltip(TOOLTIP);

        consumeMouseEvents(false);

        imageView = new ImageView();
        imageView.fitWidthProperty().bind(itemView.fitWidthProperty());
        imageView.fitHeightProperty().bind(itemView.fitHeightProperty());
        getChildren().add(imageView);

        itemView.itemProperty().addListener(observable -> update());
        itemView.playAnimationProperty().addListener(observable -> update());
        update();
    }

    public List<ItemData> getItemData() {
        return itemData;
    }

    private void update() {
        ItemView itemView = getSkinnable();

        if (timeline != null) {
            timeline.stop();
        }
        imageView.setImage(null);

        ItemRef item = itemView.getItem();
        if (item == null) {
            itemData = itemView.getItemMap().get(ItemRef.AIR);
        } else {
            itemData = itemView.getItemMap().get(item);
        }

        if (itemData.size() == 0) {
            imageView.setImage(MISSING.getImage());
            return;
        }

        if (itemView.isPlayAnimation() && itemData.size() > 1) {
            timeline = createTimeline();
            timeline.play();
        } else {
            imageView.setImage(itemData.get(0).getDisplayImage().getImage());
        }
    }

    private Timeline createTimeline() {
        Timeline timeline = new Timeline();
        ObservableList<KeyFrame> keyFrames = timeline.getKeyFrames();
        timeline.setCycleCount(Timeline.INDEFINITE);
        for (int i = 0; i < itemData.size(); i++) {
            CachedImage image = itemData.get(i).getDisplayImage();
            keyFrames.add(new KeyFrame(Duration.seconds(i), event -> imageView.setImage(image.getImage())));
        }
        return timeline;
    }
}
