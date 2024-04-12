package com.github.mouse0w0.peach.mcmod.ui.dialog;

import com.github.mouse0w0.gridview.GridView;
import com.github.mouse0w0.gridview.cell.GridCell;
import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.mcmod.IdMetadata;
import com.github.mouse0w0.peach.mcmod.ItemData;
import com.github.mouse0w0.peach.mcmod.index.Index;
import com.github.mouse0w0.peach.mcmod.index.IndexKeys;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemTooltipService;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.control.ButtonBar;
import com.github.mouse0w0.peach.ui.control.ButtonType;
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
    private final Project project;
    private final Index<IdMetadata, List<ItemData>> index;

    private final TextField filter;
    private final RadioButton defaultMode;
    private final RadioButton ignoreMetadataMode;
    private final RadioButton oreDictMode;
    private final GridView<IdMetadata> gridView;

    private final Timeline filterTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> updateItem()));

    public static IdMetadata pick(Node ownerNode, IdMetadata item, boolean enableIgnoreMetadata, boolean enableOreDict) {
        return pick(ownerNode.getScene().getWindow(), item, enableIgnoreMetadata, enableOreDict);
    }

    public static IdMetadata pick(Window window, IdMetadata item, boolean enableIgnoreMetadata, boolean enableOreDict) {
        ItemChooser itemChooser = new ItemChooser(window, item, enableIgnoreMetadata, enableOreDict);
        itemChooser.showAndWait();
        return itemChooser.getSelectedItem();
    }

    private ItemChooser(Window owner, IdMetadata item, boolean enableIgnoreMetadata, boolean enableOreDict) {
        this.project = WindowManager.getInstance().getWindow(owner).getProject();
        this.index = IndexManager.getInstance(project).getIndex(IndexKeys.ITEM);

        setTitle(AppL10n.localize("dialog.itemChooser.title"));
        setWidth(802);
        setHeight(600);
        initModality(Modality.APPLICATION_MODAL);
        initOwner(owner);

        BorderPane root = new BorderPane();

        Label filterLabel = new Label(AppL10n.localize("dialog.itemChooser.filter"));

        filter = new TextField();
        filter.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                updateItem();
                filterTimeline.stop();
                event.consume();
            }
        });
        filter.textProperty().addListener(observable -> filterTimeline.playFromStart());

        defaultMode = new RadioButton(AppL10n.localize("dialog.itemChooser.mode.default"));

        ignoreMetadataMode = new RadioButton(AppL10n.localize("dialog.itemChooser.mode.ignoreMetadata"));
        ignoreMetadataMode.setDisable(!enableIgnoreMetadata);

        oreDictMode = new RadioButton(AppL10n.localize("dialog.itemChooser.mode.oreDict"));
        oreDictMode.setDisable(!enableOreDict);

        if (item.isOreDictionary()) oreDictMode.setSelected(true);
        else if (item.isIgnoreMetadata()) ignoreMetadataMode.setSelected(true);
        else defaultMode.setSelected(true);

        ToggleGroup modeGroup = new ToggleGroup();
        modeGroup.getToggles().addAll(defaultMode, ignoreMetadataMode, oreDictMode);
        modeGroup.selectedToggleProperty().addListener(observable -> updateItem());

        HBox headerBar = new HBox(12, filterLabel, filter, defaultMode, ignoreMetadataMode, oreDictMode);
        headerBar.setPadding(new Insets(12));
        headerBar.setAlignment(Pos.CENTER_LEFT);
        root.setTop(headerBar);

        gridView = new GridView<>();
        gridView.setCellWidth(32);
        gridView.setCellHeight(32);
        gridView.setCellFactory(view -> new Cell(project));
        gridView.getSelectionModel().select(item);
        root.setCenter(gridView);

        Button ok = ButtonType.OK.createButton();
        ok.setOnAction(event -> hide());
        Button cancel = ButtonType.CANCEL.createButton();
        cancel.setOnAction(event -> {
            MultipleSelectionModel<IdMetadata> selectionModel = gridView.getSelectionModel();
            selectionModel.clearSelection();
            selectionModel.select(item);
            hide();
        });
        root.setBottom(new ButtonBar(ok, cancel));

        Scene scene = new Scene(root);
        FXUtils.addStylesheet(scene, "style/style.css");
        FXUtils.addStylesheet(scene, "style/ItemChooser.css");
        setScene(scene);

        updateItem();
    }

    public IdMetadata getSelectedItem() {
        return gridView.getSelectionModel().getSelectedItem();
    }

    private void updateItem() {
        gridView.getItems().setAll(ListUtils.filter(index.keyList(), buildItemFilter()));
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
        if (item.getId().getPath().contains(pattern)) return true;
        for (ItemData data : index.get(item)) {
            if (data.getName().contains(pattern)) {
                return true;
            }
        }
        return false;
    }

    private static class Cell extends GridCell<IdMetadata> {
        private final ItemView itemView;

        public Cell(Project project) {
            itemView = new ItemView(project, 32);
            ItemTooltipService.getInstance(project).install(itemView);
            setGraphic(itemView);
        }

        @Override
        protected void updateItem(IdMetadata item, boolean empty) {
            super.updateItem(item, empty);
            itemView.setItem(item);
        }
    }
}
