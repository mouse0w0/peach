package com.github.mouse0w0.peach.javafx.util;

import javafx.scene.control.Spinner;
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
            if (newValue.isEmpty()) return;
            try {
                int value = Integer.parseInt(newValue);
                spinner.getValueFactory().setValue(value);
            } catch (NumberFormatException e) {
                editor.setText(oldValue);
            }
        });
        editor.focusedProperty().addListener(observable -> {
            if (editor.getText().isEmpty()) {
                spinner.getValueFactory().setValue(initialValue);
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
            if (newValue.isEmpty() || endsWith(newValue, '.')) return;
            try {
                double value = Double.parseDouble(newValue);
                spinner.getValueFactory().setValue(value);
            } catch (NumberFormatException e) {
                editor.setText(oldValue);
            }
        });
        editor.focusedProperty().addListener(observable -> {
            if (editor.getText().isEmpty()) {
                spinner.getValueFactory().setValue(initialValue);
            }
        });
    }

    private static boolean endsWith(String s, char c) {
        return s.charAt(s.length() - 1) == c; // Unsafe, s.isEmpty() not check.
    }

    private Spinners() {
    }
}
