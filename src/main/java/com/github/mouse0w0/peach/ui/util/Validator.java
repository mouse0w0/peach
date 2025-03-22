package com.github.mouse0w0.peach.ui.util;

import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextInputControl;

import java.util.Collection;
import java.util.function.Predicate;

public final class Validator<T> {
    private static final InvalidationListener FOCUSED_LISTENER = observable -> {
        var focusedProperty = (ReadOnlyBooleanProperty) observable;
        var node = (Node) focusedProperty.getBean();
        var validator = getValidator(node);
        if (validator == null) return;

        if (focusedProperty.get()) {
            var invalidCheck = validator.getInvalidCheck();
            if (invalidCheck != null) {
                MessagePopup.show(node, String.format(invalidCheck.getMessage(), validator.getValue()));
            }
        } else {
            MessagePopup.hide();
            validator.validate();
        }
    };
    private static final String VALIDATOR_PROPERTY = "validator";

    private final Node node;
    private final Property<T> property;

    public static Validator<String> of(TextInputControl textInput, String message, Predicate<String> predicate) {
        return of(textInput, textInput.textProperty(), Check.of(message, predicate));
    }

    public static Validator<String> of(TextInputControl textInput, Collection<Check<? super String>> checks) {
        return new Validator<>(textInput, textInput.textProperty(), checks);
    }

    @SafeVarargs
    public static Validator<String> of(TextInputControl textInput, Check<? super String>... checks) {
        return new Validator<>(textInput, textInput.textProperty(), checks);
    }

    public static <T> Validator<T> of(Node node, Property<T> property, Collection<Check<? super T>> checks) {
        return new Validator<>(node, property, checks);
    }

    @SafeVarargs
    public static <T> Validator<T> of(Node node, Property<T> property, Check<? super T>... checks) {
        return new Validator<>(node, property, checks);
    }

    public static <T> Validator<T> of(Node node, Property<T> property) {
        return new Validator<>(node, property);
    }

    public static boolean validate(Node... nodes) {
        boolean result = true;
        Validator<?> firstInvalidValidator = null;
        for (Node node : nodes) {
            Validator<?> validator = getValidator(node);
            if (validator != null && !validator.validate()) {
                result = false;
                if (firstInvalidValidator == null) {
                    firstInvalidValidator = validator;
                }
            }
        }

        if (firstInvalidValidator != null) {
            Node node = firstInvalidValidator.getNode();
            node.requestFocus();
            if (node instanceof TextInputControl) {
                ((TextInputControl) node).selectAll();
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> Validator<T> getValidator(Node node) {
        return node.hasProperties() ? (Validator<T>) node.getProperties().get(VALIDATOR_PROPERTY) : null;
    }

    private Validator(Node node, Property<T> property, Collection<Check<? super T>> checks) {
        this(node, property);
        getChecks().addAll(checks);
    }

    @SafeVarargs
    private Validator(Node node, Property<T> property, Check<? super T>... checks) {
        this(node, property);
        getChecks().addAll(checks);
    }

    private Validator(Node node, Property<T> property) {
        if (node == null) {
            throw new NullPointerException("Node cannot be null");
        }
        if (property == null) {
            throw new NullPointerException("Property cannot be null");
        }
        if (property.getBean() != node) {
            throw new IllegalArgumentException("Property not owned by node");
        }
        if (node.getProperties().putIfAbsent(VALIDATOR_PROPERTY, this) != null) {
            throw new IllegalArgumentException("Node already has a Validator");
        }
        this.node = node;
        this.property = property;
        node.focusedProperty().addListener(FOCUSED_LISTENER);
    }

    public Node getNode() {
        return node;
    }

    public Property<T> getProperty() {
        return property;
    }

    public T getValue() {
        return property.getValue();
    }

    private ObservableList<Check<? super T>> checks;

    public ObservableList<Check<? super T>> getChecks() {
        if (checks == null) {
            checks = FXCollections.observableArrayList();
        }
        return checks;
    }

    private ReadOnlyObjectWrapper<Check<? super T>> invalidCheck;

    private ReadOnlyObjectWrapper<Check<? super T>> invalidCheckPropertyImpl() {
        if (invalidCheck == null) {
            invalidCheck = new ReadOnlyObjectWrapper<>(this, "invalidCheck");
        }
        return invalidCheck;
    }

    public ReadOnlyObjectProperty<Check<? super T>> invalidCheckProperty() {
        return invalidCheckPropertyImpl().getReadOnlyProperty();
    }

    public Check<? super T> getInvalidCheck() {
        return invalidCheckPropertyImpl().get();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean validate() {
        Object value = property.getValue();
        for (Check check : getChecks()) {
            if (!check.test(value)) {
                invalidCheckPropertyImpl().set(check);
                Check.setInvalid(node, true);
                return false;
            }
        }
        invalidCheckPropertyImpl().set(null);
        Check.setInvalid(node, false);
        return true;
    }

    public void dispose() {
        node.getProperties().remove(Validator.class);
        node.focusedProperty().removeListener(FOCUSED_LISTENER);
    }
}
