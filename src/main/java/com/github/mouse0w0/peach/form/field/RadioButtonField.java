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

    public boolean get() {
        return value.get();
    }

    @Override
    @Deprecated
    public Boolean getValue() {
        return get();
    }

    public void set(boolean value) {
        valueProperty().set(value);
    }

    @Override
    @Deprecated
    public void setValue(Boolean value) {
        set(value);
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
