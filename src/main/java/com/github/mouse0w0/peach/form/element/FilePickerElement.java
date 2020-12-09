package com.github.mouse0w0.peach.form.element;

import com.github.mouse0w0.peach.ui.control.FilePicker;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;

public class FilePickerElement extends ValueElement<String> {
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

    public FilePicker getFilePicker() {
        return (FilePicker) getEditor();
    }

    @Override
    protected Node createDefaultEditor() {
        FilePicker filePicker = new FilePicker();
        filePicker.textProperty().bindBidirectional(valueProperty());
        filePicker.promptTextProperty().bind(promptTextProperty());
        filePicker.editableProperty().bind(editableProperty());
        filePicker.disableProperty().bind(disableProperty());
        return filePicker;
    }
}
