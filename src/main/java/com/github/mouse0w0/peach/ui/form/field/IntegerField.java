package com.github.mouse0w0.peach.ui.form.field;

import com.github.mouse0w0.peach.ui.util.Spinners;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class IntegerField extends ValueField<Integer> {
    private final SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory;

    public IntegerField(int min, int max, int initialValue) {
        this(min, max, initialValue, 1);
    }

    public IntegerField(int min, int max, int initialValue, int amountToStepBy) {
        this.valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initialValue, amountToStepBy);
    }

    @Override
    public final ObjectProperty<Integer> valueProperty() {
        return valueFactory.valueProperty();
    }

    @Override
    public final Integer getValue() {
        return valueFactory.getValue();
    }

    @Override
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

    @Override
    protected Node createEditorNode() {
        Spinner<Integer> spinner = new Spinner<>(valueFactory);
        spinner.setMaxWidth(Double.MAX_VALUE);
        spinner.disableProperty().bind(disableProperty());
        Spinners.setupIntegerSpinner(spinner);
        return spinner;
    }
}
