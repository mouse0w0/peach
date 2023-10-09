package com.github.mouse0w0.peach.mcmod.ui.dialog;

import com.github.mouse0w0.gridview.GridView;
import com.github.mouse0w0.gridview.cell.GridCell;
import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.ItemRef;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.index.Indexes;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import com.github.mouse0w0.peach.util.StringUtils;
import com.github.mouse0w0.peach.window.WindowManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ItemChooser extends Stage {
    private final TextField filter;
    private final RadioButton defaultMode;
    private final RadioButton ignoreMetadataMode;
    private final RadioButton oreDictMode;
    private final GridView<ItemRef> gridView;

    private Map<ItemRef, List<Item>> itemMap;

    private ItemRef defaultItem;

    private final Timeline filterTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> updateItem()));

    public static ItemRef pick(Node ownerNode, ItemRef defaultItem, boolean enableIgnoreMetadata, boolean enableOreDict) {
        return pick(ownerNode.getScene().getWindow(), defaultItem, enableIgnoreMetadata, enableOreDict);
    }

    public static ItemRef pick(Window window, ItemRef defaultItem, boolean enableIgnoreMetadata, boolean enableOreDict) {
        ItemChooser itemChooser = new ItemChooser();
        Project project = WindowManager.getInstance().getWindow(window).getProject();
        itemChooser.init(project, defaultItem != null ? defaultItem : ItemRef.AIR, enableIgnoreMetadata, enableOreDict);
        itemChooser.initOwner(window);
        itemChooser.showAndWait();
        return itemChooser.getSelectedItem();
    }

    private ItemChooser() {
        setTitle(AppL10n.localize("dialog.itemChooser.title"));
        setWidth(802);
        setHeight(600);
        initModality(Modality.APPLICATION_MODAL);

        Label filterLabel = new Label(AppL10n.localize("dialog.itemChooser.filter"));
        filter = new TextField();
        filter.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                updateItem();
                event.consume();
            }
        });
        filter.textProperty().addListener(observable -> filterTimeline.playFromStart());

        defaultMode = new RadioButton(AppL10n.localize("dialog.itemChooser.mode.default"));
        ignoreMetadataMode = new RadioButton(AppL10n.localize("dialog.itemChooser.mode.ignoreMetadata"));
        oreDictMode = new RadioButton(AppL10n.localize("dialog.itemChooser.mode.oreDict"));

        ToggleGroup modeGroup = new ToggleGroup();
        modeGroup.getToggles().addAll(defaultMode, ignoreMetadataMode, oreDictMode);
        modeGroup.selectedToggleProperty().addListener(observable -> updateItem());

        HBox headerBar = new HBox(10, filterLabel, filter, defaultMode, ignoreMetadataMode, oreDictMode);
        headerBar.setPadding(new Insets(10));
        headerBar.setAlignment(Pos.CENTER_LEFT);

        gridView = new GridView<>();
        gridView.setCellWidth(32);
        gridView.setCellHeight(32);
        gridView.setVerticalCellSpacing(0);
        gridView.setHorizontalCellSpacing(0);
        gridView.setCellFactory(view -> new Cell());

        Button ok = new Button(AppL10n.localize("button.ok"));
        ok.setDefaultButton(true);
        ok.setOnAction(event -> hide());
        Button cancel = new Button(AppL10n.localize("button.cancel"));
        cancel.setCancelButton(true);
        cancel.setOnAction(event -> {
            MultipleSelectionModel<ItemRef> selectionModel = gridView.getSelectionModel();
            selectionModel.clearSelection();
            selectionModel.select(defaultItem);
            hide();
        });
        HBox buttonBar = new HBox(ok, cancel);
        buttonBar.getStyleClass().add("button-bar");

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(headerBar);
        borderPane.setCenter(gridView);
        borderPane.setBottom(buttonBar);

        Scene scene = new Scene(borderPane);
        FXUtils.addStylesheet(scene, "style/style.css");
        FXUtils.addStylesheet(scene, "ui/mcmod/ItemChooser.css");
        setScene(scene);
    }

    public ItemRef getDefaultItem() {
        return defaultItem;
    }

    public ItemRef getSelectedItem() {
        return gridView.getSelectionModel().getSelectedItem();
    }

    private void init(Project project, ItemRef defaultItem, boolean enableIgnoreMetadata, boolean enableOreDict) {
        this.defaultItem = defaultItem;
        this.itemMap = IndexManager.getInstance(project).getIndex(Indexes.ITEMS);

//        filter.setText(null);

        ignoreMetadataMode.setDisable(!enableIgnoreMetadata);
        oreDictMode.setDisable(!enableOreDict);

        if (defaultItem.isOreDictionary()) oreDictMode.setSelected(true);
        else if (defaultItem.isIgnoreMetadata()) ignoreMetadataMode.setSelected(true);
        else defaultMode.setSelected(true);

        updateItem();

        gridView.getSelectionModel().select(defaultItem);
    }

    private void updateItem() {
        List<ItemRef> items = itemMap.keySet()
                .stream() // 10000个元素以下时，串行比并行Stream要好。
                .filter(buildItemFilter())
                .collect(Collectors.toList());
        gridView.getItems().setAll(items);
    }

    private Predicate<ItemRef> buildItemFilter() {
        Predicate<ItemRef> predicate;
        if (ignoreMetadataMode.isSelected()) {
            predicate = ItemRef::isIgnoreMetadata;
        } else if (oreDictMode.isSelected()) {
            predicate = ItemRef::isOreDictionary;
        } else {
            predicate = ItemRef::isNormal;
        }

        final String pattern = filter.getText();
        if (StringUtils.isNotEmpty(pattern)) {
            predicate = predicate.and(item -> filterItem(item, pattern));
        }

        return predicate;
    }

    private boolean filterItem(ItemRef item, String pattern) {
        if (item.getId().contains(pattern)) return true;
        for (Item data : itemMap.get(item)) {
            if (data.getLocalizedText().contains(pattern)) {
                return true;
            }
        }
        return false;
    }

    private static class Cell extends GridCell<ItemRef> {
        private final ItemView itemView = new ItemView(32);

        public Cell() {
            setGraphic(itemView);
        }

        @Override
        protected void updateItem(ItemRef item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                itemView.setItem(null);
            } else {
                itemView.setItem(item);
            }
        }
    }
}
