package com.github.mouse0w0.peach.ui.form;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public abstract class Element {
    public static final String FORM_ELEMENT_CLASS = "form-element";

    public Element() {
        getStyleClass().add(FORM_ELEMENT_CLASS);
    }

    private ReadOnlyObjectWrapper<Group> group;

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

    private IntegerProperty colSpan;

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

    private StringProperty id;

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

    private ObservableList<String> styleClass;

    public final ObservableList<String> getStyleClass() {
        if (styleClass == null) {
            styleClass = FXCollections.observableArrayList();

        }
        return styleClass;
    }

    private BooleanProperty disable;

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

    private BooleanProperty visible;

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

    private Node node;

    public final Node getNode() {
        if (node == null) {
            node = createNode();
        }
        return node;
    }

    protected abstract Node createNode();

    public boolean validate() {
        return true;
    }
}
