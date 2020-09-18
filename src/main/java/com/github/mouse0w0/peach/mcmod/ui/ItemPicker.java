package com.github.mouse0w0.peach.mcmod.ui;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.content.ContentManager;
import com.github.mouse0w0.peach.mcmod.content.data.ItemData;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.project.WindowManager;
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

import java.util.Collections;
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

    private Project project;

    private Item defaultItem;
    private ToggleGroup selectedItem = new ToggleGroup();
    private boolean cancelled;

    private List<Node> cacheNormalEntries;
    private List<Node> cacheIgnoreMetadataEntries;
    private List<Node> cacheOreDictEntries;

    public static Item pick(Window window, Item defaultItem, boolean enableIgnoreMetadata, boolean enableOreDict) {
        if (instance == null) instance = new ItemPicker();
        Project project = WindowManager.getInstance().getWindow(window).getProject();
        instance.init(project, defaultItem, enableIgnoreMetadata, enableOreDict);
        Stage stage = new Stage();
        stage.setScene(instance.scene);
        stage.setTitle(I18n.translate("ui.item_picker.title"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(window);
        stage.showAndWait();
        return instance.getSelectedItem();
    }

    private ItemPicker() {
        scene = new Scene(FXUtils.loadFXML(null, this, "ui/mcmod/ItemPicker.fxml"));

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

        mode.selectedToggleProperty().addListener(observable -> initEntries());
    }

    private void init(Project project, Item defaultItem, boolean enableIgnoreMetadata, boolean enableOreDict) {
        setProject(project);
        this.defaultItem = defaultItem;

        filter.setText(null);

        ignoreMetadata.setDisable(!enableIgnoreMetadata);
        oreDict.setDisable(!enableOreDict);

        if (defaultItem.isOreDict()) oreDict.setSelected(true);
        else if (defaultItem.isIgnoreMetadata()) ignoreMetadata.setSelected(true);
        else normal.setSelected(true);

        selectedItem.getToggles()
                .parallelStream()
                .filter(toggle -> defaultItem.equals(((Cell) toggle).getItem()))
                .findAny()
                .ifPresent(selectedItem::selectToggle);
    }

    private void setProject(Project project) {
        if (this.project == project) return;
        this.project = project;
        cacheNormalEntries = null;
        cacheIgnoreMetadataEntries = null;
        cacheOreDictEntries = null;
        initEntries();
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
        if (!contentChildren.isEmpty()) {
            selectedItem.selectToggle((Toggle) content.getChildren().get(0));
        }
    }

    private List<Node> generateEntries(Predicate<Item> filter) {
        return project != null ? ContentManager.getInstance(project).getItemMap().keySet()
                .parallelStream()
                .filter(filter)
                .map(item -> new Cell(item, selectedItem))
                .sequential()
                .collect(Collectors.toList()) : Collections.emptyList();
    }

    private void filter(String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            content.getChildren().forEach(node -> FXUtils.setManagedAndVisible(node, true));
        } else {
            content.getChildren().forEach(node -> FXUtils.setManagedAndVisible(node, filterEntry((Cell) node, pattern)));
        }
    }

    private boolean filterEntry(Cell cell, String pattern) {
        if (cell.getItem().getId().contains(pattern)) return true;
        for (ItemData itemDatum : cell.getItemData()) {
            if (itemDatum.getDisplayName().contains(pattern)) {
                return true;
            }
        }
        return false;
    }

    public Item getSelectedItem() {
        return cancelled ? defaultItem : ((Cell) selectedItem.getSelectedToggle()).getItem();
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

    private static class Cell extends ToggleButton {

        private static final Tooltip TOOLTIP;

        private final Item item;
        private final ItemView itemView;

        static {
            TOOLTIP = new Tooltip();
            TOOLTIP.setFont(Font.font(13));
            TOOLTIP.setOnShowing(event ->
                    FXUtils.getTooltipOwnerNode().ifPresent(node -> {
                                Cell cell = (Cell) node;
                                Item item = cell.getItem();
                                List<ItemData> itemData = cell.getItemData();
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

        public Cell(Item item, ToggleGroup toggleGroup) {
            this.item = Validate.notNull(item);
            this.itemView = new ItemView(item, 32, 32);
            getStyleClass().add("cell");
            setToggleGroup(toggleGroup);
            setGraphic(itemView);
            setTooltip(TOOLTIP);
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
