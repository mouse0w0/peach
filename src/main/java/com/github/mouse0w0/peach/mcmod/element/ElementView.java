package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.gridview.GridView;
import com.github.mouse0w0.gridview.cell.GridCell;
import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.fileEditor.FileEditorManager;
import com.github.mouse0w0.peach.javafx.FXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.nio.file.Path;

public class ElementView extends GridView<Path> {

    private final ElementManager elementManager;

    private final ContextMenu cellMenu;

    public ElementView(ElementManager elementManager) {
        this.elementManager = elementManager;

        getStylesheets().add("/ui/mcmod/ElementView.css");

        cellMenu = new ContextMenu();
        MenuItem open = new MenuItem(I18n.translate("common.open"));
        open.setOnAction(event -> doOpenWizard());
        MenuItem remove = new MenuItem(I18n.translate("common.remove"));
        remove.setOnAction(event -> doDelete());
        cellMenu.getItems().addAll(open, remove);

        setCellFactory(gridView -> new Cell());
        setCellSize(200, 74);
        setCellSpacing(0, 0);

//        elementManager.getElements().addListener(new SetChangeListener<Path>() {
//            @Override
//            public void onChanged(Change<? extends Path> change) {
//                if (change.wasAdded()) getItems().add(change.getElementAdded());
//                if (change.wasRemoved()) getItems().remove(change.getElementRemoved());
//            }
//        });
//        getItems().addAll(elementManager.getElements());
    }

    public void doOpenWizard() {
        FileEditorManager.getInstance(elementManager.getProject()).open(getSelectionModel().getSelectedItem());
    }

    public void doDelete() {
        elementManager.removeElement(getSelectionModel().getSelectedItem());
    }

    private class Cell extends GridCell<Path> {
        private final Node content;

        @FXML
        private Rectangle icon;
        @FXML
        private Text title;
        @FXML
        private Text description;

        public Cell() {
            this.content = FXUtils.loadFXML(null, this, "ui/mcmod/ElementViewCell.fxml");

            setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) doOpenWizard();
            });
            setContextMenu(cellMenu);
        }

        @Override
        protected void updateItem(Path item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                ElementType<?> type = ElementRegistry.getInstance().getElementType(item);

                title.setText(ElementHelper.getElementFileName(item));
                description.setText(I18n.translate(type.getTranslationKey()));

                setGraphic(content);
            }
        }
    }
}
