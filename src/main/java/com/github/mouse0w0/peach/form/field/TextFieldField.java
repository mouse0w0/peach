package com.github.mouse0w0.peach.form.field;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.TextField;

public class TextFieldField extends ValueField<String> {
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
        this.value.setValue(value);
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
    protected Node createEditor() {
        TextField textField = new TextField();
        textField.textProperty().bindBidirectional(valueProperty());
        textField.promptTextProperty().bind(promptTextProperty());
        textField.disableProperty().bind(disableProperty());
        return textField;
    }
}
