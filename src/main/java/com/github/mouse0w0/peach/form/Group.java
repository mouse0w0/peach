package com.github.mouse0w0.peach.form;

import com.github.mouse0w0.peach.form.skin.GroupView;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;

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
            node = createDefaultNode();
        }
        return node;
    }

    protected Node createDefaultNode() {
        return new GroupView(this);
    }

    public final boolean validate() {
        for (Element element : elements) {
            if (!element.validate()) return false;
        }
        return true;
    }
}
