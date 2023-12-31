package com.github.mouse0w0.peach.mcmod.ui.dialog;

import com.github.mouse0w0.gridview.GridView;
import com.github.mouse0w0.gridview.cell.GridCell;
import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.mcmod.IdMetadata;
import com.github.mouse0w0.peach.mcmod.ItemData;
import com.github.mouse0w0.peach.mcmod.index.Index;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.index.IndexTypes;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import com.github.mouse0w0.peach.util.ListUtils;
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
import java.util.function.Predicate;

public class ItemChooser extends Stage {
    private final TextField filter;
    private final RadioButton defaultMode;
    private final RadioButton ignoreMetadataMode;
    private final RadioButton oreDictMode;
    private final GridView<IdMetadata> gridView;

    private Index<IdMetadata, List<ItemData>> itemIndex;

    private IdMetadata defaultItem;

    private final Timeline filterTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> updateItem()));

    public static IdMetadata pick(Node ownerNode, IdMetadata defaultItem, boolean enableIgnoreMetadata, boolean enableOreDict) {
        return pick(ownerNode.getScene().getWindow(), defaultItem, enableIgnoreMetadata, enableOreDict);
    }

    public static IdMetadata pick(Window window, IdMetadata defaultItem, boolean enableIgnoreMetadata, boolean enableOreDict) {
        ItemChooser itemChooser = new ItemChooser();
        Project project = WindowManager.getInstance().getWindow(window).getProject();
        itemChooser.init(project, defaultItem != null ? defaultItem : IdMetadata.AIR, enableIgnoreMetadata, enableOreDict);
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
            MultipleSelectionModel<IdMetadata> selectionModel = gridView.getSelectionModel();
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

    public IdMetadata getDefaultItem() {
        return defaultItem;
    }

    public IdMetadata getSelectedItem() {
        return gridView.getSelectionModel().getSelectedItem();
    }

    private void init(Project project, IdMetadata defaultItem, boolean enableIgnoreMetadata, boolean enableOreDict) {
        this.defaultItem = defaultItem;
        this.itemIndex = IndexManager.getInstance(project).getIndex(IndexTypes.ITEM);

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
        gridView.getItems().setAll(ListUtils.filterCollect(itemIndex.keySet(), buildItemFilter()));
    }

    private Predicate<IdMetadata> buildItemFilter() {
        Predicate<IdMetadata> predicate;
        if (ignoreMetadataMode.isSelected()) {
            predicate = IdMetadata::isIgnoreMetadata;
        } else if (oreDictMode.isSelected()) {
            predicate = IdMetadata::isOreDictionary;
        } else {
            predicate = IdMetadata::isNormal;
        }

        final String pattern = filter.getText();
        if (StringUtils.isNotEmpty(pattern)) {
            predicate = predicate.and(item -> filterItem(item, pattern));
        }

        return predicate;
    }

    private boolean filterItem(IdMetadata item, String pattern) {
        if (item.getId().contains(pattern)) return true;
        for (ItemData data : itemIndex.get(item)) {
            if (data.getName().contains(pattern)) {
                return true;
            }
        }
        return false;
    }

    private static class Cell extends GridCell<IdMetadata> {
        private final ItemView itemView = new ItemView(32);

        public Cell() {
            setGraphic(itemView);
        }

        @Override
        protected void updateItem(IdMetadata item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                itemView.setItem(null);
            } else {
                itemView.setItem(item);
            }
        }
    }
}
