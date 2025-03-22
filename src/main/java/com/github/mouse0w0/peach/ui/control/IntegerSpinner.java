package com.github.mouse0w0.peach.ui.control;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class IntegerSpinner extends Spinner<Integer> {
    private final SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory;

    public IntegerSpinner(int min, int max) {
        this(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, min));
    }

    public IntegerSpinner(int min, int max, int initialValue) {
        this(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initialValue));
    }

    public IntegerSpinner(int min, int max, int initialValue, int amountToStepBy) {
        this(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initialValue, amountToStepBy));
    }

    public IntegerSpinner(SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory) {
        super(valueFactory);
        this.valueFactory = valueFactory;

        getStyleClass().add("integer-spinner");
        setEditable(true);
        setDefaultValue(valueFactory.getValue());

        focusedProperty().addListener(observable -> {
            if (valueFactory.getValue() == null) {
                valueFactory.setValue(getDefaultValue());
            }
        });
    }

    public final void setValue(Integer value) {
        valueFactory.setValue(value);
    }

    private final IntegerProperty defaultValue = new SimpleIntegerProperty(this, "defaultValue");

    public final IntegerProperty defaultValueProperty() {
        return defaultValue;
    }

    public final int getDefaultValue() {
        return defaultValue.get();
    }

    public final void setDefaultValue(int defaultValue) {
        defaultValueProperty().set(defaultValue);
    }
}
