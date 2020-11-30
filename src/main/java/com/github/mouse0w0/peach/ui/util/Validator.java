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

public class Validator {

    public static final String WARNING = "validation-warning";
    public static final String ERROR = "validation-error";

    private static final PopupAlert POPUP_ALERT;

    private static final ChangeListener<Boolean> FOCUSED_LISTENER;

    private final Node node;
    private final Property<?> property;
    private final List<CheckItem<?>> items;

    private CheckItem<?> invalidItem;

    static {
        POPUP_ALERT = new PopupAlert();

        FOCUSED_LISTENER = (observable, oldValue, newValue) -> {
            ReadOnlyProperty<?> focusedProperty = (ReadOnlyProperty<?>) observable;
            Node bean = (Node) focusedProperty.getBean();

            Validator validator = getValidator(bean);
            if (validator == null) return;

            if (newValue) {
                CheckItem<?> item = validator.getInvalidItem();
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
        new Validator(node, new CheckItem<>(predicate, NotificationLevel.ERROR, message)).register();
    }

    public static <T> void warning(Node node, Predicate<T> predicate, String message) {
        new Validator(node, new CheckItem<>(predicate, NotificationLevel.WARNING, message)).register();
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

    public Validator(Node node, CheckItem<?>... items) {
        this.node = node;
        this.property = ValuePropertyUtils.valueProperty(node)
                .orElseThrow(() -> new IllegalArgumentException("Not found the value property of " + node.getClass()));
        Arrays.sort(items);
        this.items = ImmutableList.copyOf(items);
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
                return item.getLevel() != NotificationLevel.ERROR;
            }
        }
        updateStyleClasses();
        return true;
    }

    private void updateStyleClasses() {
        CheckItem<?> item = getInvalidItem();
        NotificationLevel type = item == null ? NotificationLevel.NONE : item.getLevel();
        ObservableList<String> styleClass = getNode().getStyleClass();
        styleClass.removeAll(ERROR, WARNING);
        if (type == NotificationLevel.ERROR) styleClass.add(ERROR);
        else if (type == NotificationLevel.WARNING) styleClass.add(WARNING);
    }
}
