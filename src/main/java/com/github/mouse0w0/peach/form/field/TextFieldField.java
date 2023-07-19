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
        valueProperty().setValue(value);
    }

    public final StringProperty promptTextProperty() {
        return getTextField().promptTextProperty();
    }

    public final String getPromptText() {
        return getTextField().getPromptText();
    }

    public final void setPromptText(String value) {
        getTextField().setPromptText(value);
    }

    public final TextField getTextField() {
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
