package com.github.mouse0w0.peach.form.field;

import com.github.mouse0w0.peach.form.Element;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;

public abstract class Field extends Element {
    public static final String FORM_FIELD_CLASS = "form-field";
    public static final String FORM_FIELD_LABEL_CLASS = "form-field-label";
    public static final String FORM_FIELD_EDITOR_CLASS = "form-field-editor";

    public static final String FIELD_PROP = "form-field";

    private StringProperty text;

    private Node editor;

    public Field() {
        getStyleClass().add(FORM_FIELD_CLASS);
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
        label.getStyleClass().add(FORM_FIELD_LABEL_CLASS);
        label.setWrapText(true);
        label.textProperty().bind(textProperty());
        return new FieldView(this, label, getEditor());
    }

    public final Node getEditor() {
        if (editor == null) {
            editor = createEditor();
            editor.getStyleClass().add(FORM_FIELD_EDITOR_CLASS);
            editor.getProperties().put(FIELD_PROP, this);
        }
        return editor;
    }

    protected abstract Node createEditor();
}
