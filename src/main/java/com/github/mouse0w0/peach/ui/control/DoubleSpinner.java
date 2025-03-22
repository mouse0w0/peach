package com.github.mouse0w0.peach.ui.control;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class DoubleSpinner extends Spinner<Double> {
    private final SpinnerValueFactory.DoubleSpinnerValueFactory valueFactory;

    public DoubleSpinner(double min, double max) {
        this(new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, min));
    }

    public DoubleSpinner(double min, double max, double initialValue) {
        this(new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, initialValue));
    }

    public DoubleSpinner(double min, double max, double initialValue, double amountToStepBy) {
        this(new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, initialValue, amountToStepBy));
    }

    public DoubleSpinner(SpinnerValueFactory.DoubleSpinnerValueFactory valueFactory) {
        super(valueFactory);
        this.valueFactory = valueFactory;

        getStyleClass().add("double-spinner");
        setEditable(true);
        setDefaultValue(valueFactory.getValue());

        focusedProperty().addListener(observable -> {
            if (valueFactory.getValue() == null) {
                valueFactory.setValue(getDefaultValue());
            }
        });
    }

    public final void setValue(Double value) {
        valueFactory.setValue(value);
    }

    private final DoubleProperty defaultValue = new SimpleDoubleProperty(this, "defaultValue");

    public final DoubleProperty defaultValueProperty() {
        return defaultValue;
    }

    public final double getDefaultValue() {
        return defaultValue.get();
    }

    public final void setDefaultValue(double defaultValue) {
        defaultValueProperty().set(defaultValue);
    }
}
