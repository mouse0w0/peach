package com.github.mouse0w0.peach.ui.util;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

public final class Spinners {
    public static Spinner<Integer> createIntegerSpinner(int min, int max) {
        return createIntegerSpinner(min, max, 0, 1);
    }

    public static Spinner<Integer> createIntegerSpinner(int min, int max, int initialValue) {
        return createIntegerSpinner(min, max, initialValue, 1);
    }

    public static Spinner<Integer> createIntegerSpinner(int min, int max, int initialValue, int amountToStepBy) {
        Spinner<Integer> spinner = new Spinner<>(min, max, initialValue, amountToStepBy);
        setupIntegerSpinner(spinner);
        return spinner;
    }

    public static void setupIntegerSpinner(Spinner<Integer> spinner) {
        spinner.setEditable(true);
        Integer initialValue = spinner.getValue();
        TextField editor = spinner.getEditor();
        editor.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty() || newValue.equals("-")) return;
            SpinnerValueFactory<Integer> valueFactory = spinner.getValueFactory();
            if (valueFactory != null) {
                try {
                    valueFactory.setValue(Integer.valueOf(newValue));
                } catch (NumberFormatException e) {
                    editor.setText(oldValue);
                }
            }
        });
        editor.focusedProperty().addListener(observable -> {
            if (editor.getText().isEmpty()) {
                SpinnerValueFactory<Integer> valueFactory = spinner.getValueFactory();
                if (valueFactory != null) {
                    valueFactory.setValue(initialValue);
                }
            }
        });
    }

    public static Spinner<Double> createDoubleSpinner(double min, double max) {
        return createDoubleSpinner(min, max, 0, 1);
    }

    public static Spinner<Double> createDoubleSpinner(double min, double max, double initialValue) {
        return createDoubleSpinner(min, max, initialValue, 1);
    }

    public static Spinner<Double> createDoubleSpinner(double min, double max, double initialValue, double amountToStepBy) {
        Spinner<Double> spinner = new Spinner<>(min, max, initialValue, amountToStepBy);
        setupDoubleSpinner(spinner);
        return spinner;
    }

    public static void setupDoubleSpinner(Spinner<Double> spinner) {
        spinner.setEditable(true);
        Double initialValue = spinner.getValue();
        TextField editor = spinner.getEditor();
        editor.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty() || newValue.equals("-") || newValue.indexOf('.') == newValue.length() - 1) return;
            SpinnerValueFactory<Double> valueFactory = spinner.getValueFactory();
            if (valueFactory != null) {
                try {
                    valueFactory.setValue(Double.valueOf(newValue));
                } catch (NumberFormatException e) {
                    editor.setText(oldValue);
                }
            }
        });
        editor.focusedProperty().addListener(observable -> {
            if (editor.getText().isEmpty()) {
                SpinnerValueFactory<Double> valueFactory = spinner.getValueFactory();
                if (valueFactory != null) {
                    valueFactory.setValue(initialValue);
                }
            }
        });
    }

    private Spinners() {
    }
}
