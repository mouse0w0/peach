package com.github.mouse0w0.peach.form.field;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;

public class RadioButtonField extends ValueField<Boolean> {
    private final BooleanProperty value = new SimpleBooleanProperty(this, "value");

    @Override
    public final BooleanProperty valueProperty() {
        return value;
    }

    public final boolean get() {
        return value.get();
    }

    @Override
    @Deprecated
    public final Boolean getValue() {
        return get();
    }

    public final void set(boolean value) {
        valueProperty().set(value);
    }

    @Override
    @Deprecated
    public final void setValue(Boolean value) {
        set(value);
    }

    public final RadioButton getRadioButton() {
        return (RadioButton) getEditor();
    }

    @Override
    protected Node createEditor() {
        RadioButton radioButton = new RadioButton();
        radioButton.selectedProperty().bindBidirectional(valueProperty());
        radioButton.disableProperty().bind(disableProperty());
        return radioButton;
    }
}
