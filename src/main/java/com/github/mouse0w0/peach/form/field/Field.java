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

    public Field() {
        getStyleClass().add(FORM_FIELD_CLASS);
    }

    private StringProperty text;

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

    private Node editorNode;

    public final Node getEditorNode() {
        if (editorNode == null) {
            editorNode = createEditorNode();
            decorateEditorNode(editorNode);
        }
        return editorNode;
    }

    protected abstract Node createEditorNode();

    protected void decorateEditorNode(Node node) {
        node.getStyleClass().add(FORM_FIELD_EDITOR_CLASS);
        node.getProperties().put(FIELD_PROP, this);
    }

    @Override
    protected Node createNode() {
        Label label = new Label();
        label.getStyleClass().add(FORM_FIELD_LABEL_CLASS);
        label.setWrapText(true);
        label.textProperty().bind(textProperty());
        return new FieldView(this, label, getEditorNode());
    }
}
