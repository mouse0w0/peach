package com.github.mouse0w0.peach.form.field;

import com.github.mouse0w0.peach.javafx.control.PopupAlert;
import com.github.mouse0w0.peach.javafx.util.Check;
import com.github.mouse0w0.peach.javafx.util.NotificationLevel;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Node;

public abstract class ValueField<T> extends Field {
    private static final PopupAlert POPUP_ALERT;

    private static final ChangeListener<Boolean> FOCUSED_LISTENER;

    private BooleanProperty editable;

    private ObservableList<Check<? super T>> checks;
    private ReadOnlyObjectWrapper<Check<? super T>> invalidCheck;

    static {
        POPUP_ALERT = new PopupAlert();

        FOCUSED_LISTENER = (observable, oldValue, newValue) -> {
            ReadOnlyProperty<?> focusedProperty = (ReadOnlyProperty<?>) observable;
            Node bean = (Node) focusedProperty.getBean();
            ValueField<?> element = getElement(bean);
            if (element == null) return;

            if (newValue) {
                Check<?> item = element.getInvalidCheck();
                if (item == null) return;

                POPUP_ALERT.setLevel(NotificationLevel.ERROR);
                POPUP_ALERT.setText(String.format(item.getMessage(), element.getValue()));
                POPUP_ALERT.show(bean, Side.TOP, 0, -3);
            } else {
                POPUP_ALERT.hide();
                element.validate();
            }
        };
    }

    private static ValueField<?> getElement(Node node) {
        return node.hasProperties() ? (ValueField<?>) node.getProperties().get(Field.FIELD) : null;
    }

    public abstract Property<T> valueProperty();

    public abstract T getValue();

    public abstract void setValue(T value);

    public final BooleanProperty editableProperty() {
        if (editable == null) {
            editable = new SimpleBooleanProperty(this, "editable", true);
        }
        return editable;
    }

    public final boolean isEditable() {
        return editable == null || editable.get();
    }

    public final void setEditable(boolean editable) {
        editableProperty().set(editable);
    }

    public final ObservableList<Check<? super T>> getChecks() {
        if (checks == null) {
            checks = FXCollections.observableArrayList();
            getEditor().focusedProperty().addListener(FOCUSED_LISTENER);
        }
        return checks;
    }

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
                updateStyleClass(false);
                return false;
            }
        }

        if (invalidCheck != null) {
            invalidCheck.set(null);
        }
        updateStyleClass(true);
        return true;
    }

    private void updateStyleClass(boolean valid) {
        ObservableList<String> styleClass = getEditor().getStyleClass();
        styleClass.remove(Check.INVALID_STYLE_CLASS);
        if (!valid) {
            styleClass.add(Check.INVALID_STYLE_CLASS);
        }
    }
}
