package com.github.mouse0w0.peach.ui.util;

import com.github.mouse0w0.peach.ui.control.PopupAlert;
import com.google.common.collect.ImmutableList;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.TextInputControl;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public final class Validator {

    public static final String WARNING = "validation-warning";
    public static final String ERROR = "validation-error";

    private static final PopupAlert POPUP_ALERT;

    private static final ChangeListener<Boolean> FOCUSED_LISTENER;

    private final Node node;
    private final Property<?> property;
    private final List<Check<?>> checks;

    private Check<?> invalidCheck;

    static {
        POPUP_ALERT = new PopupAlert();

        FOCUSED_LISTENER = (observable, oldValue, newValue) -> {
            ReadOnlyProperty<?> focusedProperty = (ReadOnlyProperty<?>) observable;
            Node bean = (Node) focusedProperty.getBean();

            Validator validator = getValidator(bean);
            if (validator == null) return;

            if (newValue) {
                Check<?> item = validator.getInvalidCheck();
                if (item == null) return;

                POPUP_ALERT.setLevel(item.getLevel());
                POPUP_ALERT.setText(String.format(item.getMessage(), validator.getProperty().getValue()));
                POPUP_ALERT.show(bean, Side.TOP, 0, -3);
            } else {
                POPUP_ALERT.hide();
                validator.test();
            }
        };
    }

    public static <T> void error(Node node, Predicate<T> predicate, String message) {
        register(node, new Check<>(predicate, NotificationLevel.ERROR, message));
    }

    public static <T> void warning(Node node, Predicate<T> predicate, String message) {
        register(node, new Check<>(predicate, NotificationLevel.WARNING, message));
    }

    public static void register(Node node, Check<?>... checks) {
        Validator validator = new Validator(node, checks);
        node.getProperties().put(Validator.class, validator);
        node.focusedProperty().addListener(FOCUSED_LISTENER);
    }

    public static void unregister(Node node) {
        Validator validator = getValidator(node);
        if (validator != null) {
            node.getProperties().remove(Validator.class);
            node.focusedProperty().removeListener(FOCUSED_LISTENER);
        }
    }

    public static boolean test(Node... nodes) {
        boolean result = true;
        Validator firstInvalid = null;
        for (Node node : nodes) {
            Validator validator = getValidator(node);
            if (validator != null && !validator.test()) {
                result = false;
                if (firstInvalid == null) {
                    firstInvalid = validator;
                }
            }
        }

        if (firstInvalid != null) {
            Node node = firstInvalid.getNode();
            node.requestFocus();
            if (node instanceof TextInputControl) {
                ((TextInputControl) node).selectAll();
            }
        }
        return result;
    }

    public static Validator getValidator(Node node) {
        if (!node.hasProperties()) {
            return null;
        }
        return (Validator) node.getProperties().get(Validator.class);
    }

    private Validator(Node node, Check<?>... checks) {
        this.node = node;
        this.property = ValuePropertyUtils.valueProperty(node)
                .orElseThrow(() -> new IllegalArgumentException("Not found the value property of " + node.getClass()));
        Arrays.sort(checks);
        this.checks = ImmutableList.copyOf(checks);
    }

    public Node getNode() {
        return node;
    }

    public Property<?> getProperty() {
        return property;
    }

    public List<Check<?>> getChecks() {
        return checks;
    }

    public Check<?> getInvalidCheck() {
        return invalidCheck;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean test() {
        invalidCheck = null;
        for (Check item : getChecks()) {
            if (!item.test(property.getValue())) {
                invalidCheck = item;
                updateStyleClasses();
                return item.getLevel() != NotificationLevel.ERROR;
            }
        }
        updateStyleClasses();
        return true;
    }

    private void updateStyleClasses() {
        Check<?> item = getInvalidCheck();
        NotificationLevel type = item == null ? NotificationLevel.NONE : item.getLevel();
        ObservableList<String> styleClass = getNode().getStyleClass();
        styleClass.removeAll(ERROR, WARNING);
        if (type == NotificationLevel.ERROR) styleClass.add(ERROR);
        else if (type == NotificationLevel.WARNING) styleClass.add(WARNING);
    }
}
