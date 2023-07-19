package com.github.mouse0w0.peach.form.field;

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
        valueProperty().setValue(value);
    }

    public final StringProperty promptTextProperty() {
        return getTextArea().promptTextProperty();
    }

    public final String getPromptText() {
        return getTextArea().getPromptText();
    }

    public final void setPromptText(String value) {
        getTextArea().setPromptText(value);
    }

    public final TextArea getTextArea() {
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
