package com.github.mouse0w0.peach.form.field;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.TextField;

public class TextFieldField extends ValueField<String> {
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

    public StringProperty promptTextProperty() {
        return getTextField().promptTextProperty();
    }

    public String getPromptText() {
        return getTextField().getPromptText();
    }

    public void setPromptText(String value) {
        getTextField().setPromptText(value);
    }

    public TextField getTextField() {
        return (TextField) getEditor();
    }

    @Override
    protected Node createEditor() {
        TextField textField = new TextField();
        textField.textProperty().bindBidirectional(valueProperty());
        textField.disableProperty().bind(disableProperty());
        return textField;
    }
}
