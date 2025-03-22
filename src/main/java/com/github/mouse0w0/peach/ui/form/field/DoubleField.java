package com.github.mouse0w0.peach.ui.form.field;

import com.github.mouse0w0.peach.ui.control.DoubleSpinner;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.SpinnerValueFactory;

public class DoubleField extends ValueField<Double> {
    private final SpinnerValueFactory.DoubleSpinnerValueFactory valueFactory;

    public DoubleField(double min, double max, double initialValue) {
        this(min, max, initialValue, 1);
    }

    public DoubleField(double min, double max, double initialValue, double amountToStepBy) {
        this.valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, initialValue, amountToStepBy);
    }

    @Override
    public final ObjectProperty<Double> valueProperty() {
        return valueFactory.valueProperty();
    }

    @Override
    public final Double getValue() {
        return valueFactory.getValue();
    }

    @Override
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

    @Override
    protected Node createEditorNode() {
        DoubleSpinner spinner = new DoubleSpinner(valueFactory);
        spinner.setMaxWidth(Double.MAX_VALUE);
        spinner.disableProperty().bind(disableProperty());
        return spinner;
    }
}
