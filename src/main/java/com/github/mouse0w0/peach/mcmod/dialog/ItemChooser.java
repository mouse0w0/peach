package com.github.mouse0w0.peach.mcmod.dialog;

import com.github.mouse0w0.gridview.GridView;
import com.github.mouse0w0.gridview.cell.GridCell;
import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.javafx.FXUtils;
import com.github.mouse0w0.peach.mcmod.ItemRef;
import com.github.mouse0w0.peach.mcmod.content.data.ItemData;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.index.StandardIndexes;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.wm.WindowManager;
import com.google.common.base.Strings;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ItemChooser {

    private static ItemChooser instance;

    private Map<ItemRef, List<ItemData>> itemMap;

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
    private GridView<ItemRef> gridView;

    private ItemRef defaultItem;

    private final Timeline filterTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> updateItem()));

    public static ItemRef pick(Node ownerNode, ItemRef defaultItem, boolean enableIgnoreMetadata, boolean enableOreDict) {
        return pick(ownerNode.getScene().getWindow(), defaultItem, enableIgnoreMetadata, enableOreDict);
    }

    public static ItemRef pick(Window window, ItemRef defaultItem, boolean enableIgnoreMetadata, boolean enableOreDict) {
        if (instance == null) instance = new ItemChooser();
        Project project = WindowManager.getInstance().getWindow(window).getProject();
        instance.init(project, defaultItem != null ? defaultItem : ItemRef.AIR, enableIgnoreMetadata, enableOreDict);
        Stage stage = new Stage();
        stage.setScene(instance.scene);
        stage.setTitle(I18n.translate("dialog.itemChooser.title"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(window);
        stage.showAndWait();
        return instance.getSelectedItem();
    }

    private ItemChooser() {
        scene = new Scene(FXUtils.loadFXML(null, this, "ui/mcmod/ItemChooser.fxml"));

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

    public ItemRef getDefaultItem() {
        return defaultItem;
    }

    public ItemRef getSelectedItem() {
        return gridView.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void onFinish() {
        FXUtils.hideWindow(scene);
    }

    @FXML
    private void onCancel() {
        MultipleSelectionModel<ItemRef> selectionModel = gridView.getSelectionModel();
        selectionModel.clearSelection();
        selectionModel.select(defaultItem);
        FXUtils.hideWindow(scene);
    }

    private void init(Project project, ItemRef defaultItem, boolean enableIgnoreMetadata, boolean enableOreDict) {
        this.defaultItem = defaultItem;
        this.itemMap = IndexManager.getInstance(project).getIndex(StandardIndexes.ITEMS);

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
        List<ItemRef> items = itemMap.keySet()
                .stream() // 10000个元素以下时，串行比并行Stream要好。
                .filter(buildItemFilter())
                .collect(Collectors.toList());
        gridView.getItems().setAll(items);
    }

    private Predicate<ItemRef> buildItemFilter() {
        Predicate<ItemRef> predicate;
        if (ignoreMetadata.isSelected()) {
            predicate = ItemRef::isIgnoreMetadata;
        } else if (oreDict.isSelected()) {
            predicate = ItemRef::isOreDict;
        } else {
            predicate = ItemRef::isNormal;
        }

        final String pattern = filter.getText();
        if (!Strings.isNullOrEmpty(pattern)) {
            predicate = predicate.and(item -> filterItem(item, pattern));
        }

        return predicate;
    }

    private boolean filterItem(ItemRef item, String pattern) {
        if (item.getId().contains(pattern)) return true;
        for (ItemData data : itemMap.get(item)) {
            if (data.getDisplayName().contains(pattern)) {
                return true;
            }
        }
        return false;
    }

    private class Cell extends GridCell<ItemRef> {
        private final ItemView itemView = new ItemView(32, 32);

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
