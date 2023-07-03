package com.github.mouse0w0.peach.form.field;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;

public class RadioButtonField extends ValueField<Boolean> {
    private final BooleanProperty value = new SimpleBooleanProperty(this, "value");

    @Override
    public BooleanProperty valueProperty() {
        return value;
    }

    @Override
    public Boolean getValue() {
        return value.get();
    }

    @Override
    public void setValue(Boolean value) {
        valueProperty().setValue(value);
    }

    @Override
    protected Node createEditor() {
        RadioButton radioButton = new RadioButton();
        radioButton.textProperty().bind(promptTextProperty());
        radioButton.selectedProperty().bindBidirectional(valueProperty());
        radioButton.disableProperty().bind(disableProperty());
        return radioButton;
    }
}
