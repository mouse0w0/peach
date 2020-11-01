package com.github.mouse0w0.peach.ui.validation;

import com.github.mouse0w0.peach.ui.util.ValuePropertyUtils;
import javafx.beans.property.Property;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public final class Validator {
    private final ValidationHandler[] handlers;

    private final List<Item> items = new ArrayList<>();
    private final List<Item> invalidItems = new ArrayList<>();

    public Validator() {
        this(ValidationHandler.EMPTY);
    }

    public Validator(ValidationHandler... handlers) {
        this.handlers = handlers;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Item> getInvalidItems() {
        return invalidItems;
    }

    public <T> void register(Node node, Predicate<T> predicate, String message) {
        Property<Object> property = ValuePropertyUtils.valueProperty(node)
                .orElseThrow(() -> new IllegalArgumentException("Not found the value property of " + node.getClass()));
        Item item = new Item(node, property, predicate, message);
        items.add(item);
        fireRegister(item);
    }

    private void fireRegister(Item item) {
        for (ValidationHandler handler : handlers) {
            handler.onRegister(item);
        }
    }

    public void unregister(Node node) {
        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (item.getNode() == node) {
                iterator.remove();
                fireUnregister(item);
            }
        }
    }

    private void fireUnregister(Item item) {
        for (ValidationHandler handler : handlers) {
            handler.onUnregister(item);
        }
    }

    public boolean validate() {
        reset();
        for (Item item : items) {
            if (!item.validate()) {
                invalidItems.add(item);
            }
        }

        if (isValid()) {
            fireValid();
        } else {
            fireInvalid();
        }
        return isValid();
    }

    private void fireValid() {
        for (ValidationHandler handler : handlers) {
            handler.onValid(this);
        }
    }

    private void fireInvalid() {
        for (ValidationHandler handler : handlers) {
            handler.onInvalid(this);
        }
    }

    public boolean isValid() {
        return invalidItems.isEmpty();
    }

    private void reset() {
        for (ValidationHandler handler : handlers) {
            handler.onReset(this);
        }
        invalidItems.clear();
    }

    public static class Item {
        private final Node node;
        private final Property property;
        private final Predicate predicate;
        private final String message;

        public Item(Node node, Property<?> property, Predicate<?> predicate, String message) {
            this.node = node;
            this.property = property;
            this.predicate = predicate;
            this.message = message;
        }

        public Node getNode() {
            return node;
        }

        public Object getValue() {
            return property.getValue();
        }

        public String getMessage() {
            return message;
        }

        public boolean validate() {
            return predicate.test(property.getValue());
        }
    }
}
