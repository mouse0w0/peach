package com.github.mouse0w0.peach.javafx;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

public final class Spinners {

    public static Spinner<Integer> create(int min, int max, int initialValue) {
        Spinner<Integer> spinner = new Spinner<>(min, max, initialValue);
        spinner.setEditable(true);
        setupIntegerEditor(spinner, initialValue);
        return spinner;
    }

    public static void setupIntegerEditor(Spinner<Integer> spinner, int initialValue) {
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

    public static Spinner<Double> create(double min, double max, double initialValue) {
        Spinner<Double> spinner = new Spinner<>(min, max, initialValue);
        spinner.setEditable(true);
        setupDoubleEditor(spinner, initialValue);
        return spinner;
    }

    public static void setupDoubleEditor(Spinner<Double> spinner, double initialValue) {
        TextField editor = spinner.getEditor();
        editor.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty() || newValue.equals("-") || newValue.indexOf('.') == newValue.lastIndexOf('.'))
                return;
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
