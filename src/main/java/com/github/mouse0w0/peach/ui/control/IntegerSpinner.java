package com.github.mouse0w0.peach.ui.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

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
        getEditor().setTextFormatter(new TextFormatter<>(change -> {
            String text = change.getControlNewText();
            if (text.isEmpty()) {
                return change;
            }
            if (getMin() < 0 && "-".equals(text)) {
                return change;
            }
            int value;
            try {
                value = Integer.parseInt(text);
            } catch (NumberFormatException e) {
                return null;
            }
            if (value < getMin() || value > getMax()) {
                return null;
            }
            return change;
        }));
    }

    public final void setValue(Integer value) {
        valueFactory.setValue(value);
    }

    public final IntegerProperty minProperty() {
        return valueFactory.minProperty();
    }

    public final int getMin() {
        return valueFactory.getMin();
    }

    public final void setMin(int value) {
        valueFactory.setMin(value);
    }

    public final IntegerProperty maxProperty() {
        return valueFactory.maxProperty();
    }

    public final int getMax() {
        return valueFactory.getMax();
    }

    public final void setMax(int value) {
        valueFactory.setMax(value);
    }

    public final IntegerProperty amountToStepByProperty() {
        return valueFactory.amountToStepByProperty();
    }

    public final int getAmountToStepBy() {
        return valueFactory.getAmountToStepBy();
    }

    public final void setAmountToStepBy(int value) {
        valueFactory.setAmountToStepBy(value);
    }

    public final ObjectProperty<StringConverter<Integer>> converterProperty() {
        return valueFactory.converterProperty();
    }

    public final StringConverter<Integer> getConverter() {
        return valueFactory.getConverter();
    }

    public final void setConverter(StringConverter<Integer> newValue) {
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
