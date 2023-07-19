package com.github.mouse0w0.peach.form.field;

import com.github.mouse0w0.peach.javafx.control.FilePicker;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;

public class FilePickerField extends ValueField<String> {
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
        return getFilePicker().promptTextProperty();
    }

    public String getPromptText() {
        return getFilePicker().getPromptText();
    }

    public void setPromptText(String value) {
        getFilePicker().setPromptText(value);
    }

    public FilePicker getFilePicker() {
        return (FilePicker) getEditor();
    }

    @Override
    protected Node createEditor() {
        FilePicker filePicker = new FilePicker();
        filePicker.valueProperty().bindBidirectional(valueProperty());
        filePicker.editableProperty().bind(editableProperty());
        filePicker.disableProperty().bind(disableProperty());
        return filePicker;
    }
}
