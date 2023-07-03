package com.github.mouse0w0.peach.form.field;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

public class ColorPickerField extends ValueField<Color> {
    private final ObjectProperty<Color> value = new SimpleObjectProperty<>(this, "value");

    @Override
    public ObjectProperty<Color> valueProperty() {
        return value;
    }

    @Override
    public Color getValue() {
        return value.get();
    }

    @Override
    public void setValue(Color value) {
        valueProperty().setValue(value);
    }

    @Override
    protected Node createEditor() {
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        colorPicker.valueProperty().bindBidirectional(valueProperty());
        colorPicker.disableProperty().bind(disableProperty());
        return colorPicker;
    }
}
