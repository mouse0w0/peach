package com.github.mouse0w0.peach.javafx;

import it.unimi.dsi.fastutil.Pair;
import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public final class ValuePropertyUtils {
    private static final List<Pair<Predicate<Node>, Function<Node, Property<?>>>> VALUE_PROPERTY_GETTERS = new ArrayList<>();

    public static void register(Predicate<Node> predicate, Function<Node, Property<?>> getter) {
        VALUE_PROPERTY_GETTERS.add(Pair.of(predicate, getter));
    }

    static {
        register(n -> n instanceof TextInputControl, n -> ((TextInputControl) n).textProperty());
        register(n -> n instanceof ComboBoxBase, n -> ((ComboBoxBase<?>) n).valueProperty());
        register(n -> n instanceof ChoiceBox, n -> ((ChoiceBox<?>) n).valueProperty());
        register(n -> n instanceof Spinner, n -> ((Spinner<?>) n).getValueFactory().valueProperty());
        register(n -> n instanceof CheckBox, n -> ((CheckBox) n).selectedProperty());
        register(n -> n instanceof Toggle, n -> ((Toggle) n).selectedProperty());
        register(n -> n instanceof Slider, n -> ((Slider) n).valueProperty());
    }

    public static <T> Optional<Property<T>> valueProperty(Node node) {
        for (Pair<Predicate<Node>, Function<Node, Property<?>>> pair : VALUE_PROPERTY_GETTERS) {
            if (pair.left().test(node)) {
                return Optional.of((Property<T>) pair.right().apply(node));
            }
        }
        return Optional.empty();
    }

    public static <T> T getValue(Node node) {
        return (T) valueProperty(node).orElseThrow(UnsupportedOperationException::new).getValue();
    }

    public static void setValue(Node node, Object value) {
        valueProperty(node).orElseThrow(UnsupportedOperationException::new).setValue(value);
    }

    private ValuePropertyUtils() {
    }
}
