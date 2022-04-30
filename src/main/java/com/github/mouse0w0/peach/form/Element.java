package com.github.mouse0w0.peach.form;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public abstract class Element {
    private ReadOnlyObjectWrapper<Group> group;

    private ObjectProperty<ColSpan> colSpan;

    private BooleanProperty disable;
    private BooleanProperty visible;

    private StringProperty id;
    private ObservableList<String> styleClass;

    private ReadOnlyBooleanWrapper valid;

    private Node node;

    public Element() {
        getStyleClass().add("form-element");
    }

    public final Node getNode() {
        if (node == null) {
            node = createDefaultNode();
        }
        return node;
    }

    protected abstract Node createDefaultNode();

    final ReadOnlyObjectWrapper<Group> groupPropertyImpl() {
        if (group == null) {
            group = new ReadOnlyObjectWrapper<>(this, "group");
        }
        return group;
    }

    public final ReadOnlyObjectProperty<Group> groupProperty() {
        return groupPropertyImpl().getReadOnlyProperty();
    }

    public final Group getGroup() {
        return group != null ? group.get() : null;
    }

    public final ObjectProperty<ColSpan> colSpanProperty() {
        if (colSpan == null) {
            colSpan = new SimpleObjectProperty<>(this, "colSpan", ColSpan.ONE);
        }
        return colSpan;
    }

    public final ColSpan getColSpan() {
        return colSpan != null ? colSpan.get() : ColSpan.ONE;
    }

    public final void setColSpan(ColSpan colSpan) {
        colSpanProperty().set(colSpan);
    }

    public final BooleanProperty disableProperty() {
        if (disable == null) {
            disable = new SimpleBooleanProperty(this, "disable", false);
        }
        return disable;
    }

    public final boolean isDisable() {
        return disable != null && disable.get();
    }

    public final void setDisable(boolean disable) {
        disableProperty().set(disable);
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

    private ReadOnlyBooleanWrapper validPropertyImpl() {
        if (valid == null) {
            valid = new ReadOnlyBooleanWrapper(this, "valid", true);
        }
        return valid;
    }

    public final ReadOnlyBooleanProperty validProperty() {
        return validPropertyImpl().getReadOnlyProperty();
    }

    public final boolean isValid() {
        return valid == null || valid.get();
    }

    protected final void setValid(boolean valid) {
        validPropertyImpl().set(valid);
    }

    public boolean validate() {
        return true;
    }
}
