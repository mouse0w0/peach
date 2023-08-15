package com.github.mouse0w0.peach.ui.form.field;

import com.github.mouse0w0.peach.ui.util.Check;
import com.github.mouse0w0.peach.ui.util.MessagePopup;
import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public abstract class ValueField<T> extends Field {
    private static final String FORM_VALUE_FIELD = "form-value-field";

    private static final InvalidationListener FOCUSED_LISTENER = observable -> {
        var focusedProperty = (ReadOnlyBooleanProperty) observable;
        var node = (Node) focusedProperty.getBean();
        var valueField = (ValueField<?>) node.getProperties().get(FORM_VALUE_FIELD);

        if (valueField == null) return;

        if (focusedProperty.get()) {
            var invalidCheck = valueField.getInvalidCheck();
            if (invalidCheck != null) {
                MessagePopup.show(node, String.format(invalidCheck.getMessage(), valueField.getValue()));
            }
        } else {
            MessagePopup.hide();
            valueField.validate();
        }
    };

    public abstract Property<T> valueProperty();

    public abstract T getValue();

    public abstract void setValue(T value);

    private ObservableList<Check<? super T>> checks;

    public final ObservableList<Check<? super T>> getChecks() {
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

    public final ReadOnlyObjectProperty<Check<? super T>> invalidCheckProperty() {
        return invalidCheckPropertyImpl().getReadOnlyProperty();
    }

    public final Check<? super T> getInvalidCheck() {
        return invalidCheck != null ? invalidCheck.get() : null;
    }

    @Override
    public final boolean validate() {
        if (checks == null) return true;

        T value = getValue();
        for (Check<? super T> check : checks) {
            if (!check.test(value)) {
                invalidCheckPropertyImpl().set(check);
                Check.setInvalid(getEditorNode(), true);
                return false;
            }
        }

        if (invalidCheck != null) {
            invalidCheck.set(null);
        }
        Check.setInvalid(getEditorNode(), false);
        return true;
    }

    @Override
    protected void decorateEditorNode(Node node) {
        super.decorateEditorNode(node);
        node.getProperties().put(FORM_VALUE_FIELD, this);
        node.focusedProperty().addListener(FOCUSED_LISTENER);
    }
}
