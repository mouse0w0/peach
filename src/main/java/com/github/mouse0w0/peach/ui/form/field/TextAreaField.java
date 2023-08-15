package com.github.mouse0w0.peach.ui.form.field;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.TextArea;

public class TextAreaField extends ValueField<String> {
    private final StringProperty value = new SimpleStringProperty(this, "value");

    @Override
    public final StringProperty valueProperty() {
        return value;
    }

    @Override
    public final String getValue() {
        return value.get();
    }

    @Override
    public final void setValue(String value) {
        this.value.set(value);
    }

    private final StringProperty promptText = new SimpleStringProperty(this, "promptText");

    public final StringProperty promptTextProperty() {
        return promptText;
    }

    public final String getPromptText() {
        return promptText.get();
    }

    public final void setPromptText(String value) {
        promptText.set(value);
    }

    @Override
    protected Node createEditorNode() {
        TextArea textArea = new TextArea();
        textArea.textProperty().bindBidirectional(valueProperty());
        textArea.promptTextProperty().bind(promptTextProperty());
        textArea.disableProperty().bind(disableProperty());
        return textArea;
    }
}
