package com.github.mouse0w0.peach.ui.validation;

import com.github.mouse0w0.peach.ui.util.Alerts;
import com.github.mouse0w0.peach.ui.util.ValuePropertyUtils;
import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.TextInputControl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class Validator {

    private final List<Entry> entries = new ArrayList<>();
    private final List<Entry> invalidEntries = new ArrayList<>();

    public List<Entry> getInvalidEntries() {
        return invalidEntries;
    }

    public <T> void register(Node node, Predicate<T> predicate, String message) {
        Property<Object> property = ValuePropertyUtils.valueProperty(node)
                .orElseThrow(() -> new IllegalArgumentException("Not found the value property of " + node.getClass()));
        entries.add(new Entry(node, property, predicate, message));
    }

    public void unregister(Node node) {
        entries.removeIf(entry -> entry.getNode() == node);
    }

    public boolean validate() {
        invalidEntries.clear();
        for (Entry entry : entries) {
            if (!entry.validate()) {
                invalidEntries.add(entry);
            }
        }
        return invalidEntries.isEmpty();
    }

    public void showInvalidDialog() {
        if (invalidEntries.isEmpty()) return;

        StringBuilder message = new StringBuilder();
        for (Entry entry : invalidEntries) {
            message.append(String.format(entry.getMessage(), entry.getValue())).append("\n");
        }
        Alerts.error(message.toString());
    }

    public void focusFirstInvalid() {
        Node firstInvalid = invalidEntries.get(0).getNode();
        firstInvalid.requestFocus();
        if (firstInvalid instanceof TextInputControl) {
            ((TextInputControl) firstInvalid).selectAll();
        }
    }

    public static class Entry {
        private final Node node;
        private final Property property;
        private final Predicate predicate;
        private final String message;

        public Entry(Node node, Property<?> property, Predicate<?> predicate, String message) {
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
