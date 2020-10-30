package com.github.mouse0w0.peach.mcmod.dialog;

import com.github.mouse0w0.gridview.GridView;
import com.github.mouse0w0.gridview.cell.GridCell;
import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.content.ContentManager;
import com.github.mouse0w0.peach.mcmod.content.data.ItemData;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.project.WindowManager;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import com.google.common.base.Strings;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ItemPicker {

    private static ItemPicker instance;

    private ContentManager contentManager;

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
    private GridView<Item> gridView;

    private Item defaultItem;

    private final Timeline filterTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> updateItem()));

    private final Tooltip tooltip = createTooltip();

    public static Item pick(Window window, Item defaultItem, boolean enableIgnoreMetadata, boolean enableOreDict) {
        if (instance == null) instance = new ItemPicker();
        Project project = WindowManager.getInstance().getWindow(window).getProject();
        instance.init(project, defaultItem != null ? defaultItem : Item.AIR, enableIgnoreMetadata, enableOreDict);
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

        gridView.setCellWidth(32);
        gridView.setCellHeight(32);
        gridView.setVerticalCellSpacing(0);
        gridView.setHorizontalCellSpacing(0);
        gridView.setCellFactory(view -> new Cell());

        filter.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) updateItem();
        });
        filter.textProperty().addListener(observable -> filterTimeline.playFromStart());

        mode.selectedToggleProperty().addListener(observable -> updateItem());
    }

    public Item getDefaultItem() {
        return defaultItem;
    }

    public Item getSelectedItem() {
        return gridView.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void onFinish() {
        FXUtils.hideWindow(scene);
    }

    @FXML
    private void onCancel() {
        MultipleSelectionModel<Item> selectionModel = gridView.getSelectionModel();
        selectionModel.clearSelection();
        selectionModel.select(defaultItem);
        FXUtils.hideWindow(scene);
    }

    private void init(Project project, Item defaultItem, boolean enableIgnoreMetadata, boolean enableOreDict) {
        this.defaultItem = defaultItem;
        contentManager = ContentManager.getInstance(project);

//        filter.setText(null);

        ignoreMetadata.setDisable(!enableIgnoreMetadata);
        oreDict.setDisable(!enableOreDict);

        if (defaultItem.isOreDict()) oreDict.setSelected(true);
        else if (defaultItem.isIgnoreMetadata()) ignoreMetadata.setSelected(true);
        else normal.setSelected(true);

        updateItem();

        gridView.getSelectionModel().select(defaultItem);
    }

    private void updateItem() {
        List<Item> items = contentManager.getItems()
                .parallelStream()
                .filter(buildItemFilter())
                .collect(Collectors.toList());
        gridView.getItems().setAll(items);
    }

    private Predicate<Item> buildItemFilter() {
        Predicate<Item> predicate;
        if (ignoreMetadata.isSelected()) {
            predicate = Item::isIgnoreMetadata;
        } else if (oreDict.isSelected()) {
            predicate = Item::isOreDict;
        } else {
            predicate = Item::isNormal;
        }

        final String pattern = filter.getText();
        if (!Strings.isNullOrEmpty(pattern)) {
            predicate = predicate.and(item -> filterItem(item, pattern));
        }

        return predicate;
    }

    private boolean filterItem(Item item, String pattern) {
        if (item.getId().contains(pattern)) return true;
        for (ItemData data : contentManager.getItemData(item)) {
            if (data.getDisplayName().contains(pattern)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private Tooltip createTooltip() {
        Tooltip tooltip = new Tooltip();
        tooltip.setOnShowing(event ->
                FXUtils.getTooltipOwnerNode().ifPresent(node -> {
                            Item item = ((GridCell<Item>) node).getItem();
                            if (item == null) tooltip.hide();

                            StringBuilder sb = new StringBuilder();

                            sb.append(item.getId());
                            if (item.isNormal()) sb.append("#").append(item.getMetadata());

                            sb.append("\n--------------------\n");

                            for (ItemData data : contentManager.getItemData(item)) {
                                sb.append(data.getDisplayName()).append("\n");
                            }

                            tooltip.setText(sb.substring(0, sb.length() - 1));
                        }
                ));
        return tooltip;
    }

    private class Cell extends GridCell<Item> {
        private final ItemView itemView = new ItemView(32, 32);

        public Cell() {
            setGraphic(itemView);
            setTooltip(tooltip);
        }

        @Override
        protected void updateItem(Item item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                itemView.setItem(null);
            } else {
                itemView.setItem(item);
            }
        }
    }
}
