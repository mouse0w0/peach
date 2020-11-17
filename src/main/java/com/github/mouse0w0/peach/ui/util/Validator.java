package com.github.mouse0w0.peach.ui.util;

import com.github.mouse0w0.peach.ui.control.PopupAlert;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.TextInputControl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Validator {

    public static final String WARNING = "validation-warning";
    public static final String ERROR = "validation-error";

    private static final PopupAlert POPUP_ALERT;

    private static final ChangeListener<Boolean> FOCUSED_LISTENER;

    private final Node node;
    private final Property<?> property;
    private final List<CheckItem<?>> items = new ArrayList<>(1);

    private CheckItem<?> invalidItem;

    static {
        POPUP_ALERT = new PopupAlert();

        FOCUSED_LISTENER = (observable, oldValue, newValue) -> {
            if (newValue) {
                ReadOnlyProperty<?> focusedProperty = (ReadOnlyProperty<?>) observable;
                Node bean = (Node) focusedProperty.getBean();
                Validator validator = getValidator(bean);
                if (validator == null) return;

                CheckItem<?> item = validator.getInvalidItem();
                if (item == null) return;

                POPUP_ALERT.setType(item.getAlertType());
                POPUP_ALERT.setText(String.format(item.getMessage(), validator.getProperty().getValue()));
                POPUP_ALERT.show(bean, Side.TOP, 0, -3);
            } else {
                POPUP_ALERT.hide();
            }
        };
    }

    public static <T> Validator registerError(Node node, Predicate<T> predicate, String message) {
        Validator validator = new Validator(node, predicate, PopupAlert.Type.ERROR, message);
        validator.register();
        return validator;
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

    private static Validator getValidator(Node node) {
        if (!node.hasProperties()) return null;
        return (Validator) node.getProperties().get(Validator.class);
    }

    public <T> Validator(Node node, Predicate<T> predicate, PopupAlert.Type type, String message) {
        this(node);
        getItems().add(new CheckItem<>(predicate, type, message));
    }

    public Validator(Node node) {
        this.node = node;
        this.property = ValuePropertyUtils.valueProperty(node)
                .orElseThrow(() -> new IllegalArgumentException("Not found the value property of " + node.getClass()));
    }

    public Node getNode() {
        return node;
    }

    public Property<?> getProperty() {
        return property;
    }

    public List<CheckItem<?>> getItems() {
        return items;
    }

    public CheckItem<?> getInvalidItem() {
        return invalidItem;
    }

    public void register() {
        node.getProperties().put(Validator.class, this);
        node.focusedProperty().addListener(FOCUSED_LISTENER);
    }

    public void unregister() {
        node.getProperties().remove(Validator.class);
        node.focusedProperty().removeListener(FOCUSED_LISTENER);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean test() {
        invalidItem = null;
        for (CheckItem item : getItems()) {
            if (!item.test(property.getValue())) {
                invalidItem = item;
                updateStyleClasses();
                return false;
            }
        }
        updateStyleClasses();
        return true;
    }

    private void updateStyleClasses() {
        CheckItem<?> item = getInvalidItem();
        PopupAlert.Type type = item == null ? PopupAlert.Type.NONE : item.getAlertType();
        ObservableList<String> styleClass = getNode().getStyleClass();
        styleClass.removeAll(ERROR, WARNING);
        if (type == PopupAlert.Type.ERROR) styleClass.add(ERROR);
        else if (type == PopupAlert.Type.WARNING) styleClass.add(WARNING);
    }

    public static class CheckItem<T> {
        private final Predicate<T> predicate;
        private final PopupAlert.Type alertType;
        private final String message;

        public CheckItem(Predicate<T> predicate, PopupAlert.Type alertType, String message) {
            this.predicate = predicate;
            this.alertType = alertType;
            this.message = message;
        }

        public Predicate<T> getPredicate() {
            return predicate;
        }

        public PopupAlert.Type getAlertType() {
            return alertType;
        }

        public String getMessage() {
            return message;
        }

        public boolean test(T value) {
            return predicate.test(value);
        }
    }
}
