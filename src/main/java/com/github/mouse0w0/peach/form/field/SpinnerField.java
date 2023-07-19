package com.github.mouse0w0.peach.form.field;

import com.github.mouse0w0.peach.javafx.util.Spinners;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class SpinnerField<T extends Number> extends ValueField<T> {
    private final ObjectProperty<T> value = new SimpleObjectProperty<>(this, "value");

    public SpinnerField(int min, int max, int initialValue) {
        this(min, max, initialValue, 1);
    }

    @SuppressWarnings("unchecked")
    public SpinnerField(int min, int max, int initialValue, int amountToStepBy) {
        this((SpinnerValueFactory<T>) new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initialValue, amountToStepBy));
        Spinners.setupIntegerEditor((Spinner<Integer>) getSpinner());
    }

    public SpinnerField(double min, double max, double initialValue) {
        this(min, max, initialValue, 1);
    }

    @SuppressWarnings("unchecked")
    public SpinnerField(double min, double max, double initialValue, double amountToStepBy) {
        this((SpinnerValueFactory<T>) new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, initialValue, amountToStepBy));
        Spinners.setupDoubleEditor((Spinner<Double>) getSpinner());
    }

    public SpinnerField(SpinnerValueFactory<T> valueFactory) {
        setValueFactory(valueFactory);
    }

    @Override
    public final ObjectProperty<T> valueProperty() {
        return value;
    }

    @Override
    public final T getValue() {
        return value.get();
    }

    @Override
    public final void setValue(T value) {
        valueProperty().setValue(value);
    }

    public final ObjectProperty<SpinnerValueFactory<T>> valueFactoryProperty() {
        return getSpinner().valueFactoryProperty();
    }

    public final SpinnerValueFactory<T> getValueFactory() {
        return getSpinner().getValueFactory();
    }

    public final void setValueFactory(SpinnerValueFactory<T> valueFactory) {
        getSpinner().setValueFactory(valueFactory);
    }

    @SuppressWarnings("unchecked")
    public final Spinner<T> getSpinner() {
        return (Spinner<T>) getEditor();
    }

    @Override
    protected Node createEditor() {
        Spinner<T> spinner = new Spinner<>();
        spinner.setMinSize(0, 0);
        spinner.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        spinner.valueFactoryProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                valueProperty().unbindBidirectional(oldValue.valueProperty());
            }

            if (newValue != null) {
                valueProperty().bindBidirectional(newValue.valueProperty());
            }
        });
        spinner.disableProperty().bind(disableProperty());
        return spinner;
    }
}
