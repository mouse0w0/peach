package com.github.mouse0w0.peach.ui.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.StringConverter;

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

    public final DoubleProperty minProperty() {
        return valueFactory.minProperty();
    }

    public final double getMin() {
        return valueFactory.getMin();
    }

    public final void setMin(double value) {
        valueFactory.setMin(value);
    }

    public final DoubleProperty maxProperty() {
        return valueFactory.maxProperty();
    }

    public final double getMax() {
        return valueFactory.getMax();
    }

    public final void setMax(double value) {
        valueFactory.setMax(value);
    }

    public final DoubleProperty amountToStepByProperty() {
        return valueFactory.amountToStepByProperty();
    }

    public final double getAmountToStepBy() {
        return valueFactory.getAmountToStepBy();
    }

    public final void setAmountToStepBy(double value) {
        valueFactory.setAmountToStepBy(value);
    }

    public final ObjectProperty<StringConverter<Double>> converterProperty() {
        return valueFactory.converterProperty();
    }

    public final StringConverter<Double> getConverter() {
        return valueFactory.getConverter();
    }

    public final void setConverter(StringConverter<Double> newValue) {
        valueFactory.setConverter(newValue);
    }

    public final BooleanProperty wrapAroundProperty() {
        return valueFactory.wrapAroundProperty();
    }

    public final boolean isWrapAround() {
        return valueFactory.isWrapAround();
    }

    public final void setWrapAround(boolean value) {
        valueFactory.setWrapAround(value);
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
