package com.github.mouse0w0.peach.javafx.util;

import com.github.mouse0w0.peach.javafx.control.PopupAlert;
import com.google.common.collect.ImmutableList;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.TextInputControl;

import java.util.List;
import java.util.function.Predicate;

public final class Validator<T> {
    private static final PopupAlert POPUP_ALERT;

    private static final ChangeListener<Boolean> FOCUSED_LISTENER;

    private final Node node;
    private final Property<T> property;
    private final List<Check<T>> checks;
    private Check<T> invalidCheck;

    static {
        POPUP_ALERT = new PopupAlert();

        FOCUSED_LISTENER = (observable, oldValue, newValue) -> {
            var property = (Property<?>) observable;
            var node = (Node) property.getBean();
            var validator = getValidator(node);
            if (validator == null) return;

            if (newValue) {
                Check<?> invalidCheck = validator.getInvalidCheck();
                if (invalidCheck != null) {
                    POPUP_ALERT.setLevel(NotificationLevel.ERROR);
                    POPUP_ALERT.setText(String.format(invalidCheck.getMessage(), validator.getProperty().getValue()));
                    POPUP_ALERT.show(node, Side.TOP, 0, -3);
                }
            } else {
                POPUP_ALERT.hide();
                validator.validate();
            }
        };
    }

    public static void register(TextInputControl textInput, String message, Predicate<String> predicate) {
        register(textInput, textInput.textProperty(), Check.of(message, predicate));
    }

    @SafeVarargs
    public static <T> void register(Node node, Property<T> property, Check<T>... checks) {
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
    private Validator(Node node, Property<T> property, Check<T>... checks) {
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

    public List<Check<T>> getChecks() {
        return checks;
    }

    public Check<T> getInvalidCheck() {
        return invalidCheck;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean validate() {
        Object value = property.getValue();
        for (Check check : getChecks()) {
            if (!check.test(value)) {
                invalidCheck = check;
                updateStyleClass(false);
                return false;
            }
        }
        invalidCheck = null;
        updateStyleClass(true);
        return true;
    }

    private void updateStyleClass(boolean valid) {
        ObservableList<String> styleClass = getNode().getStyleClass();
        styleClass.remove(Check.INVALID_STYLE_CLASS);
        if (!valid) {
            styleClass.add(Check.INVALID_STYLE_CLASS);
        }
    }
}
