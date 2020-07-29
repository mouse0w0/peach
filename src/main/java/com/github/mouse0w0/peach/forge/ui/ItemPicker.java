package com.github.mouse0w0.peach.forge.ui;

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
import java.util.stream.Collectors;

public class ItemPicker {

    private static ItemPicker instance;

    private Scene scene;

    @FXML
    private TextField filter;

    @FXML
    private ToggleGroup mode;
    @FXML
    private RadioButton normal;
    @FXML
    private RadioButton ignoreMetadata;
    @FXML
    private RadioButton oreDict;

    @FXML
    private FlowPane content;

    private ScheduledFuture<?> filterFuture;

    private Item defaultItem;
    private ToggleGroup selectedItem = new ToggleGroup();
    private boolean cancelled;

    private List<Node> cacheNormalEntries;
    private List<Node> cacheIgnoreMetadataEntries;
    private List<Node> cacheOreDictEntries;

    public static Item pick(Window window, Item defaultItem, boolean enableIgnoreMetadata, boolean enableOreDict) {
        if (instance == null) instance = new ItemPicker();
        instance.init(defaultItem, enableIgnoreMetadata, enableOreDict);
        Stage stage = new Stage();
        stage.setScene(instance.scene);
        stage.setTitle(I18n.translate("ui.item_picker.title"));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(window);
        stage.showAndWait();
        return instance.getSelectedItem();
    }

    private ItemPicker() {
        scene = new Scene(FXUtils.loadFXML(null, this, "ui/forge/ItemPicker.fxml"));

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

    private void init(Item defaultItem, boolean enableIgnoreMetadata, boolean enableOreDict) {
        this.defaultItem = defaultItem;

        ignoreMetadata.setDisable(!enableIgnoreMetadata);
        oreDict.setDisable(!enableOreDict);

        if (defaultItem.isOreDict()) oreDict.setSelected(true);
        else if (defaultItem.isIgnoreMetadata()) ignoreMetadata.setSelected(true);
        else normal.setSelected(true);

        selectedItem.getToggles()
                .parallelStream()
                .filter(toggle -> defaultItem.equals(((Entry) toggle).getItem()))
                .findAny()
                .ifPresent(selectedItem::selectToggle);
    }

    private void initEntries() {
        ObservableList<Node> contentChildren = content.getChildren();
        if (ignoreMetadata.isSelected()) {
            if (cacheIgnoreMetadataEntries == null) {
                cacheIgnoreMetadataEntries = generateEntries(Item::isIgnoreMetadata);
            }
            contentChildren.setAll(cacheIgnoreMetadataEntries);
        } else if (oreDict.isSelected()) {
            if (cacheOreDictEntries == null) {
                cacheOreDictEntries = generateEntries(Item::isOreDict);
            }
            contentChildren.setAll(cacheOreDictEntries);
        } else {
            if (cacheNormalEntries == null) {
                cacheNormalEntries = generateEntries(Item::isNormal);
            }
            contentChildren.setAll(cacheNormalEntries);
        }
        selectedItem.selectToggle((Toggle) content.getChildren().get(0));
    }

    private List<Node> generateEntries(Predicate<Item> filter) {
        return ContentManager.getInstance().getItemTokenMap().keySet()
                .parallelStream()
                .filter(filter)
                .map(itemToken -> new Entry(itemToken, selectedItem))
                .sequential()
                .collect(Collectors.toList());
    }

    private void filter(String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            content.getChildren().forEach(node -> FXUtils.setManagedAndVisible(node, true));
        } else {
            content.getChildren().forEach(node -> FXUtils.setManagedAndVisible(node, filterEntry((Entry) node, pattern)));
        }
    }

    private boolean filterEntry(Entry entry, String pattern) {
        if (entry.getItem().getId().contains(pattern)) return true;
        for (ItemData itemDatum : entry.getItemData()) {
            if (itemDatum.getDisplayName().contains(pattern)) {
                return true;
            }
        }
        return false;
    }

    public Item getSelectedItem() {
        return cancelled ? defaultItem : ((Entry) selectedItem.getSelectedToggle()).getItem();
    }

    @FXML
    private void onFinish() {
        FXUtils.hideWindow(scene);
    }

    @FXML
    private void onCancel() {
        cancelled = true;
        FXUtils.hideWindow(scene);
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

                                sb.append(item.getId());
                                if (item.isNormal()) {
                                    sb.append(":").append(item.getMetadata());
                                }

                                sb.append("\n--------------------\n");

                                for (ItemData itemDatum : itemData) {
                                    sb.append(itemDatum.getDisplayName()).append("\n");
                                }

                                TOOLTIP.setText(sb.substring(0, sb.length() - 1));
                            }
                    ));
        }

        public Entry(Item item, ToggleGroup toggleGroup) {
            this.item = Validate.notNull(item);
            this.itemView = new ItemView(item, 32, 32);
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
