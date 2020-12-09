package com.github.mouse0w0.peach.form.element;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.TextField;

public class TextFieldElement extends ValueElement<String> {
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
    protected Node createDefaultEditor() {
        TextField textField = new TextField();
        textField.textProperty().bindBidirectional(valueProperty());
        textField.promptTextProperty().bind(promptTextProperty());
        textField.editableProperty().bind(editableProperty());
        textField.disableProperty().bind(disableProperty());
        return textField;
    }
}
