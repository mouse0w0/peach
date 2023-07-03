package com.github.mouse0w0.peach.form.field;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.TextArea;

public class TextAreaField extends ValueField<String> {
    private final StringProperty value = new SimpleStringProperty(this, "value");

    @Override
    public StringProperty valueProperty() {
        return value;
    }

    @Override
    public String getValue() {
        return value.get();
    }

    @Override
    public void setValue(String value) {
        valueProperty().setValue(value);
    }

    @Override
    protected Node createEditor() {
        TextArea textArea = new TextArea();
        textArea.textProperty().bindBidirectional(valueProperty());
        textArea.promptTextProperty().bind(promptTextProperty());
        textArea.editableProperty().bind(editableProperty());
        textArea.disableProperty().bind(disableProperty());
        return textArea;
    }
}
