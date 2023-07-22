package com.github.mouse0w0.peach.form;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class Group {

    private final ObservableList<Element> elements = FXCollections.observableArrayList();

    private ReadOnlyObjectWrapper<Form> form;

    private BooleanProperty visible;

    private StringProperty id;
    private ObservableList<String> styleClass;

    private Node node;

    public Group() {
        getStyleClass().setAll("form-group");

        getElements().addListener((ListChangeListener<Element>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Element element : c.getAddedSubList()) {
                        element.groupPropertyImpl().set(Group.this);
                    }
                }
                if (c.wasRemoved()) {
                    for (Element element : c.getRemoved()) {
                        element.groupPropertyImpl().set(null);
                    }
                }
            }
        });
    }

    final ReadOnlyObjectWrapper<Form> formPropertyImpl() {
        if (form == null) {
            form = new ReadOnlyObjectWrapper<>(this, "form");
        }
        return form;
    }

    public final ReadOnlyObjectProperty<Form> formProperty() {
        return formPropertyImpl().getReadOnlyProperty();
    }

    public final Form getForm() {
        return form != null ? form.get() : null;
    }

    public ObservableList<Element> getElements() {
        return elements;
    }

    public final BooleanProperty visibleProperty() {
        if (visible == null) {
            visible = new SimpleBooleanProperty(this, "visible", true);
        }
        return visible;
    }

    public final boolean isVisible() {
        return visible == null || visible.get();
    }

    public final void setVisible(boolean visible) {
        visibleProperty().set(visible);
    }

    public final StringProperty idProperty() {
        if (id == null) {
            id = new SimpleStringProperty(this, "id");
        }
        return id;
    }

    public final String getId() {
        return id != null ? id.get() : null;
    }

    public final void setId(String id) {
        idProperty().set(id);
    }

    public final ObservableList<String> getStyleClass() {
        if (styleClass == null) {
            styleClass = FXCollections.observableArrayList();
        }
        return styleClass;
    }

    public final Node getNode() {
        if (node == null) {
            node = createNode();
        }
        return node;
    }

    protected Node createNode() {
        GridPane gridPane = new GridPane();
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.getColumnConstraints().setAll(Utils.COLUMN_CONSTRAINTS);
        gridPane.visibleProperty().bind(visibleProperty());
        gridPane.managedProperty().bind(visibleProperty());
        gridPane.idProperty().bind(idProperty());
        Bindings.bindContent(gridPane.getStyleClass(), getStyleClass());
        getElements().addListener((ListChangeListener<? super Element>) observable -> {
            Utils.layoutElements(gridPane, getElements());
        });
        Utils.layoutElements(gridPane, getElements());
        return gridPane;
    }

    public final boolean validate() {
        for (Element element : elements) {
            if (!element.validate()) {
                return false;
            }
        }
        return true;
    }
}
