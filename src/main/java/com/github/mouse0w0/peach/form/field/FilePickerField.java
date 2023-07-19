package com.github.mouse0w0.peach.form.field;

import com.github.mouse0w0.peach.javafx.control.FilePicker;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;

public class FilePickerField extends ValueField<String> {
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
        return getFilePicker().promptTextProperty();
    }

    public final String getPromptText() {
        return getFilePicker().getPromptText();
    }

    public final void setPromptText(String value) {
        getFilePicker().setPromptText(value);
    }

    public final FilePicker getFilePicker() {
        return (FilePicker) getEditor();
    }

    @Override
    protected Node createEditor() {
        FilePicker filePicker = new FilePicker();
        filePicker.valueProperty().bindBidirectional(valueProperty());
        filePicker.disableProperty().bind(disableProperty());
        return filePicker;
    }
}
