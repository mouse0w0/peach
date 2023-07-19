package com.github.mouse0w0.peach.form.field;

import com.github.mouse0w0.peach.form.Element;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;

public abstract class Field extends Element {
    public static final String FIELD = "form-field";

    private StringProperty text;

    private Node editor;

    public Field() {
        getStyleClass().add("form-field");
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

    @Override
    protected Node createNode() {
        Label label = new Label();
        label.getStyleClass().add("form-field-label");
        label.setWrapText(true);
        label.textProperty().bind(textProperty());
        return new FieldView(this, label, getEditor());
    }

    public final Node getEditor() {
        if (editor == null) {
            editor = createEditor();
            editor.getStyleClass().add("form-field-editor");
            editor.getProperties().put(FIELD, this);
        }
        return editor;
    }

    protected abstract Node createEditor();
}
