package com.github.mouse0w0.peach.javafx;

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
    private static final List<Predicate<Node>> PREDICATES = new ArrayList<>();
    private static final List<Function<Node, Property<?>>> GETTERS = new ArrayList<>();

    static {
        register(n -> n instanceof TextInputControl, n -> ((TextInputControl) n).textProperty());
        register(n -> n instanceof ComboBoxBase, n -> ((ComboBoxBase<?>) n).valueProperty());
        register(n -> n instanceof ChoiceBox, n -> ((ChoiceBox<?>) n).valueProperty());
        register(n -> n instanceof Spinner, n -> ((Spinner<?>) n).getValueFactory().valueProperty());
        register(n -> n instanceof CheckBox, n -> ((CheckBox) n).selectedProperty());
        register(n -> n instanceof Toggle, n -> ((Toggle) n).selectedProperty());
        register(n -> n instanceof Slider, n -> ((Slider) n).valueProperty());
    }

    public static void register(Predicate<Node> predicate, Function<Node, Property<?>> getter) {
        PREDICATES.add(predicate);
        GETTERS.add(getter);
    }

    public static <T> Optional<Property<T>> valueProperty(Node node) {
        for (int i = 0, size = PREDICATES.size(); i < size; i++) {
            if (PREDICATES.get(i).test(node)) {
                return Optional.of((Property<T>) GETTERS.get(i).apply(node));
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
