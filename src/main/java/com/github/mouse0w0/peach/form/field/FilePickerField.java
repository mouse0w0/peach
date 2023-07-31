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
        this.value.set(value);
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
    protected Node createEditorNode() {
        FilePicker filePicker = new FilePicker();
        filePicker.valueProperty().bindBidirectional(valueProperty());
        filePicker.promptTextProperty().bind(promptTextProperty());
        filePicker.disableProperty().bind(disableProperty());
        return filePicker;
    }
}
