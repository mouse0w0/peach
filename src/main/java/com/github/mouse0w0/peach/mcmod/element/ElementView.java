package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import com.github.mouse0w0.peach.ui.util.SkinUtils;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ElementView extends ScrollPane {

    private final ElementManager elementManager;

    private FlowPane content;

    private Entry openingMenuEntry;
    private ContextMenu entryMenu;

    public ElementView(ElementManager elementManager) {
        this.elementManager = elementManager;
        setFitToWidth(true);
        content = new FlowPane();
        setContent(content);

        entryMenu = new ContextMenu();
        MenuItem open = new MenuItem(I18n.translate("common.open"));
        open.setOnAction(event -> openingMenuEntry.doOpenWizard());
        MenuItem remove = new MenuItem(I18n.translate("common.remove"));
        remove.setOnAction(event -> openingMenuEntry.doDelete());
        entryMenu.getItems().addAll(open, remove);

        elementManager.getElements().forEach(this::addEntry);
        elementManager.getElements().addListener(new SetChangeListener<Element<?>>() {
            @Override
            public void onChanged(Change<? extends Element<?>> change) {
                if (change.wasAdded()) addEntry(change.getElementAdded());
                if (change.wasRemoved()) removeEntry(change.getElementRemoved());
            }
        });
    }

    private void addEntry(Element<?> element) {
        content.getChildren().add(new Entry(element));
    }

    private void removeEntry(Element<?> element) {
        content.getChildren().removeIf(node -> ((Entry) node).getElement().equals(element));
    }

    private class Entry extends Control {
        private final Element<?> element;

        @FXML
        private Rectangle icon;
        @FXML
        private Text title;
        @FXML
        private Text description;

        public Entry(Element<?> element) {
            this.element = element;
            setSkin(SkinUtils.create(this, FXUtils.loadFXML(null, this, "ui/mcmod/ElementViewEntry.fxml")));

            title.setText(element.getName());
            description.setText(I18n.translate(element.getType().getTranslationKey()));

            setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) doOpenWizard();
            });
            setOnContextMenuRequested(event -> openingMenuEntry = this);
            setContextMenu(entryMenu);
        }

        public Element<?> getElement() {
            return element;
        }

        public void doOpenWizard() {
            elementManager.editElement(element);
        }

        public void doDelete() {
            elementManager.removeElement(element);
        }
    }
}
