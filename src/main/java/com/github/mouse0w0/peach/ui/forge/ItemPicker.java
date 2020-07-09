package com.github.mouse0w0.peach.ui.forge;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.forge.Item;
import com.github.mouse0w0.peach.forge.contentPack.ContentManager;
import com.github.mouse0w0.peach.forge.contentPack.data.ItemData;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import com.github.mouse0w0.peach.util.ScheduleUtils;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class ItemPicker {

    private BorderPane root;

    @FXML
    private TextField filter;

    @FXML
    private ToggleGroup mode;

    @FXML
    private FlowPane content;

    private ToggleGroup selectedItem = new ToggleGroup();
    private ScheduledFuture<?> filterFuture;

    private boolean cancelled;

    public static ItemPicker show(Window window) {
        Stage stage = new Stage();
        ItemPicker itemPicker = new ItemPicker();
        Scene scene = new Scene(itemPicker.root);
        stage.setScene(scene);
        stage.setTitle(I18n.translate("ui.item_picker.title"));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(window);
        stage.showAndWait();
        return itemPicker;
    }

    public enum Mode {
        CANCELLED,
        DEFAULT,
        IGNORE_METADATA,
        ORE_DICT;
    }

    public ItemPicker() {
        root = FXUtils.loadFXML(null, this, "ui/forge/ItemPicker.fxml");

        filter.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                filter(filter.getText());
            }
        });
        filter.textProperty().addListener(observable -> {
            if (filterFuture != null && !filterFuture.isDone()) {
                filterFuture.cancel(false);
            }
            String pattern = filter.getText();
            Runnable runnable = () -> Platform.runLater(() -> filter(pattern));
            filterFuture = ScheduleUtils.schedule(runnable, 500, TimeUnit.MILLISECONDS);
        });
        initEntries();
        mode.selectedToggleProperty().addListener(observable -> initEntries());
    }

    private void initEntries() {
        ObservableList<Node> contentChildren = content.getChildren();
        contentChildren.clear();
        Predicate<Item> filter;
        switch (getSelectedMode()) {
            case DEFAULT:
            default:
                filter = Item::isStandard;
                break;
            case IGNORE_METADATA:
                filter = Item::isIgnoreMetadata;
                break;
            case ORE_DICT:
                filter = Item::isOreDict;
                break;
        }
        ContentManager.getInstance().getItemTokenMap().keySet()
                .parallelStream().filter(filter).map(itemToken -> new Entry(itemToken, selectedItem))
                .sequential().forEach(contentChildren::add);
        selectedItem.selectToggle((Toggle) content.getChildren().get(0));
    }

    private void filter(String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            content.getChildren().forEach(node -> FXUtils.setManagedAndVisible(node, true));
        } else {
            content.getChildren().forEach(node -> {
                Entry entry = (Entry) node;
                Item item = entry.getItem();
                ItemData itemData = entry.getItemData().get(0);
                if (item.getId().contains(pattern) || itemData.getDisplayName().contains(pattern)) {
                    FXUtils.setManagedAndVisible(node, true);
                } else {
                    FXUtils.setManagedAndVisible(node, false);
                }
            });
        }
    }

    public Item getSelectedItem() {
        return ((Entry) selectedItem.getSelectedToggle()).getItem();
    }

    public Mode getSelectedMode() {
        return cancelled ? Mode.CANCELLED : Mode.valueOf(((Node) mode.getSelectedToggle()).getAccessibleRoleDescription());
    }

    @FXML
    private void onFinish() {
        FXUtils.hideWindow(root);
    }

    @FXML
    private void onCancel() {
        cancelled = true;
        FXUtils.hideWindow(root);
    }

    private static class Entry extends ToggleButton {

        private static final Tooltip TOOLTIP;

        private final Item item;
        private final ItemView itemView;

        static {
            TOOLTIP = new Tooltip();
            TOOLTIP.setFont(Font.font(13));
            TOOLTIP.setOnShowing(event ->
                    FXUtils.getTooltipOwnerNode().ifPresent(node -> {
                                Entry entry = (Entry) node;
                                Item item = entry.getItem();
                                List<ItemData> itemData = entry.getItemData();
                                StringBuilder sb = new StringBuilder();

                                sb.append(item.getId()).append("\n--------------------\n");

                                for (ItemData itemDatum : itemData) {
                                    sb.append(itemDatum.getDisplayName()).append("\n");
                                }

                                TOOLTIP.setText(sb.substring(0, sb.length() - 1));
                            }
                    ));
        }

        public Entry(Item item, ToggleGroup toggleGroup) {
            this.item = Validate.notNull(item);
            itemView = new ItemView(item, 32, 32);
            getStyleClass().add("entry");
            setToggleGroup(toggleGroup);
            setGraphic(itemView);
            setTooltip(TOOLTIP);
            FXUtils.setFixedSize(this, 32, 32);
        }

        public Item getItem() {
            return item;
        }

        public List<ItemData> getItemData() {
            return itemView.getItemData();
        }

        @Override
        public void fire() {
            // we don't toggle from selected to not selected if part of a group
            if (getToggleGroup() == null || !isSelected()) {
                super.fire();
            }
        }
    }
}
