package com.github.mouse0w0.peach.javafx.util;

import com.google.common.collect.ImmutableList;
import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.TextInputControl;

import java.util.List;
import java.util.function.Predicate;

public final class Validator<T> {
    private static final InvalidationListener FOCUSED_LISTENER;

    private final Node node;
    private final Property<T> property;
    private final List<Check<? super T>> checks;
    private Check<? super T> invalidCheck;

    static {
        FOCUSED_LISTENER = observable -> {
            var focusedProperty = (ReadOnlyBooleanProperty) observable;
            var node = (Node) focusedProperty.getBean();
            var validator = getValidator(node);
            if (validator == null) return;

            if (focusedProperty.get()) {
                var invalidCheck = validator.getInvalidCheck();
                if (invalidCheck != null) {
                    MessagePopup.show(node, String.format(invalidCheck.getMessage(), validator.getProperty().getValue()));
                }
            } else {
                MessagePopup.hide();
                validator.validate();
            }
        };
    }

    public static void register(TextInputControl textInput, String message, Predicate<String> predicate) {
        register(textInput, textInput.textProperty(), Check.of(message, predicate));
    }

    @SafeVarargs
    public static <T> void register(Node node, Property<T> property, Check<? super T>... checks) {
        node.getProperties().put(Validator.class, new Validator<>(node, property, checks));
        node.focusedProperty().addListener(FOCUSED_LISTENER);
    }

    public static void unregister(Node node) {
        if (!node.hasProperties()) return;
        if (node.getProperties().remove(Validator.class) == null) return;
        node.focusedProperty().removeListener(FOCUSED_LISTENER);
    }

    public static boolean validate(Node... nodes) {
        boolean result = true;
        Validator<?> invalidated = null;
        for (Node node : nodes) {
            Validator<?> validator = getValidator(node);
            if (validator != null && !validator.validate()) {
                result = false;
                if (invalidated == null) {
                    invalidated = validator;
                }
            }
        }

        if (invalidated != null) {
            Node node = invalidated.getNode();
            node.requestFocus();
            if (node instanceof TextInputControl) {
                ((TextInputControl) node).selectAll();
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> Validator<T> getValidator(Node node) {
        return node.hasProperties() ? (Validator<T>) node.getProperties().get(Validator.class) : null;
    }

    @SafeVarargs
    private Validator(Node node, Property<T> property, Check<? super T>... checks) {
        this.node = node;
        this.property = property;
        this.checks = ImmutableList.copyOf(checks);
    }

    public Node getNode() {
        return node;
    }

    public Property<T> getProperty() {
        return property;
    }

    public List<Check<? super T>> getChecks() {
        return checks;
    }

    public Check<? super T> getInvalidCheck() {
        return invalidCheck;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean validate() {
        Object value = property.getValue();
        for (Check check : getChecks()) {
            if (!check.test(value)) {
                invalidCheck = check;
                Check.setInvalid(node, true);
                return false;
            }
        }
        invalidCheck = null;
        Check.setInvalid(node, false);
        return true;
    }

}
