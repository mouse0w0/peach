package com.github.mouse0w0.peach.form;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;

public abstract class Element {

    private ReadOnlyObjectWrapper<Group> group;

    private StringProperty text;
    private StringProperty promptText;

    private ObjectProperty<ColSpan> colSpan;

    private BooleanProperty disable;
    private BooleanProperty visible;

    private StringProperty id;
    private ObservableList<String> styleClass;

    private ReadOnlyBooleanWrapper valid;

    private Node label;
    private Node editor;

    public Element() {
        getStyleClass().add("form-element");
    }

    public final Node getLabel() {
        if (label == null) {
            label = createDefaultLabel();
        }
        return label;
    }

    protected Node createDefaultLabel() {
        Label label = new Label();
        label.setWrapText(true);
        label.textProperty().bind(textProperty());
        return label;
    }

    public final Node getEditor() {
        if (editor == null) {
            editor = createDefaultEditor();
            editor.getProperties().put(Element.class, this);
        }
        return editor;
    }

    protected abstract Node createDefaultEditor();

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

    public final StringProperty promptTextProperty() {
        if (promptText == null) {
            promptText = new SimpleStringProperty(this, "promptText");
        }
        return promptText;
    }

    public final String getPromptText() {
        return promptText != null ? promptText.get() : null;
    }

    public final void setPromptText(String promptText) {
        promptTextProperty().set(promptText);
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
