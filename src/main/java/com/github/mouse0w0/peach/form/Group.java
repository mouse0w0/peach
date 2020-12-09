package com.github.mouse0w0.peach.form;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;

public class Group {

    private final ObservableList<Element> elements = FXCollections.observableArrayList();

    private ReadOnlyObjectWrapper<Form> form;

    private StringProperty text;

    private BooleanProperty collapsible;
    private BooleanProperty expanded;
    private BooleanProperty visible;

    private StringProperty id;
    private ObservableList<String> styleClass;

    private Node node;

    public Group() {
        getStyleClass().addAll("form-group", "titled-pane");

        getElements().addListener(new ListChangeListener<Element>() {
            @Override
            public void onChanged(Change<? extends Element> c) {
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

    public final StringProperty textProperty() {
        if (text == null) {
            text = new SimpleStringProperty(this, "text");
        }
        return text;
    }

    public final String getText() {
        return text != null ? text.get() : null;
    }

    public final void setText(String text) {
        textProperty().set(text);
    }

    public final BooleanProperty collapsibleProperty() {
        if (collapsible == null) {
            collapsible = new SimpleBooleanProperty(this, "collapsible", true);
        }
        return collapsible;
    }

    public final boolean isCollapsible() {
        return collapsible == null || collapsible.get();
    }

    public final void setCollapsible(boolean collapsible) {
        collapsibleProperty().set(collapsible);
    }

    public final BooleanProperty expandedProperty() {
        if (expanded == null) {
            expanded = new SimpleBooleanProperty(this, "expanded", true);
        }
        return expanded;
    }

    public final boolean isExpanded() {
        return expanded == null || expanded.get();
    }

    public final void setExpanded(boolean expanded) {
        expandedProperty().set(expanded);
    }

    public final BooleanProperty visibleProperty() {
        if (visible == null) {
            return new SimpleBooleanProperty(this, "visible", true);
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
        TitledPane titledPane = new TitledPane();
        titledPane.textProperty().bind(textProperty());
        titledPane.collapsibleProperty().bind(collapsibleProperty());
        titledPane.expandedProperty().bind(expandedProperty());
        titledPane.visibleProperty().bind(visibleProperty());
        titledPane.managedProperty().bind(visibleProperty());
        titledPane.idProperty().bind(idProperty());
        Bindings.bindContent(titledPane.getStyleClass(), getStyleClass());
        return node;
    }

    public final boolean validate() {
        for (Element element : elements) {
            if (!element.validate()) return false;
        }
        return true;
    }
}
