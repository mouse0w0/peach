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

    public StringProperty promptTextProperty() {
        return getTextArea().promptTextProperty();
    }

    public String getPromptText() {
        return getTextArea().getPromptText();
    }

    public void setPromptText(String value) {
        getTextArea().setPromptText(value);
    }

    public TextArea getTextArea() {
        return (TextArea) getEditor();
    }

    @Override
    protected Node createEditor() {
        TextArea textArea = new TextArea();
        textArea.textProperty().bindBidirectional(valueProperty());
        textArea.disableProperty().bind(disableProperty());
        return textArea;
    }
}
