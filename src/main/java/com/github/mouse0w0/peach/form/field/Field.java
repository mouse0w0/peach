package com.github.mouse0w0.peach.form.field;

import com.github.mouse0w0.peach.form.Element;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;

public abstract class Field extends Element {
    private StringProperty text;
    private StringProperty promptText;

    private Node editor;

    public Field() {
    }

    @Override
    protected Node createDefaultNode() {
        Label label = new Label();
        label.setWrapText(true);
        label.textProperty().bind(textProperty());
        return new FieldView(this, label, getEditor());
    }

    public final Node getEditor() {
        if (editor == null) {
            editor = createDefaultEditor();
            editor.getProperties().put(Field.class, this);
        }
        return editor;
    }

    protected abstract Node createDefaultEditor();

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
}
