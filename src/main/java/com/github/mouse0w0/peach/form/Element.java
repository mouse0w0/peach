package com.github.mouse0w0.peach.form;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public abstract class Element {
    private ReadOnlyObjectWrapper<Group> group;

    private IntegerProperty colSpan;

    private BooleanProperty disable;
    private BooleanProperty visible;

    private StringProperty id;
    private ObservableList<String> styleClass;

    private Node node;

    public Element() {
        getStyleClass().add("form-item");
    }

    public final Node getNode() {
        if (node == null) {
            node = createNode();
        }
        return node;
    }

    protected abstract Node createNode();

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

    public final IntegerProperty colSpanProperty() {
        if (colSpan == null) {
            colSpan = new SimpleIntegerProperty(this, "colSpan", 12);
        }
        return colSpan;
    }

    public final int getColSpan() {
        return colSpan != null ? colSpan.get() : 12;
    }

    public final void setColSpan(int colSpan) {
        colSpanProperty().set(colSpan);
    }

    public final void setColSpan(ColSpan colSpan) {
        colSpanProperty().set(colSpan.getSpan());
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

    public boolean validate() {
        return true;
    }
}
