package com.github.mouse0w0.peach.javafx;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public final class ValuePropertyUtils {
    private static final Map<Predicate<Node>, Function<Node, Property<?>>> VALUE_PROPERTY_GETTERS =
            new HashMap<>();

    public static void register(Predicate<Node> predicate, Function<Node, Property<?>> getter) {
        VALUE_PROPERTY_GETTERS.put(predicate, getter);
    }

    static {
        register(n -> n instanceof TextInputControl, n -> ((TextInputControl) n).textProperty());
        register(n -> n instanceof ComboBox, n -> ((ComboBox<?>) n).valueProperty());
        register(n -> n instanceof ChoiceBox, n -> ((ChoiceBox<?>) n).valueProperty());
        register(n -> n instanceof CheckBox, n -> ((CheckBox) n).selectedProperty());
        register(n -> n instanceof ToggleButton, n -> ((ToggleButton) n).selectedProperty());
        register(n -> n instanceof Slider, n -> ((Slider) n).valueProperty());
        register(n -> n instanceof ColorPicker, n -> ((ColorPicker) n).valueProperty());
        register(n -> n instanceof DatePicker, n -> ((DatePicker) n).valueProperty());
        register(n -> n instanceof Spinner, n -> ((Spinner<?>) n).getValueFactory().valueProperty());

        // Failed to select items in those controls.
        register(n -> n instanceof ListView, n -> ((ListView<?>) n).itemsProperty());
        register(n -> n instanceof TableView, n -> ((TableView<?>) n).itemsProperty());
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<Property<T>> valueProperty(Node node) {
        for (Map.Entry<Predicate<Node>, Function<Node, Property<?>>> entry : VALUE_PROPERTY_GETTERS.entrySet()) {
            if (entry.getKey().test(node)) {
                return Optional.of((Property<T>) entry.getValue().apply(node));
            }
        }
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getValue(Node node) {
        return (T) valueProperty(node).orElseThrow(UnsupportedOperationException::new).getValue();
    }

    public static void setValue(Node node, Object value) {
        valueProperty(node).orElseThrow(UnsupportedOperationException::new).setValue(value);
    }

    private ValuePropertyUtils() {
    }
}
