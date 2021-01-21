package com.github.mouse0w0.peach.form.field;

import com.github.mouse0w0.peach.form.Element;
import com.github.mouse0w0.peach.javafx.control.PopupAlert;
import com.github.mouse0w0.peach.javafx.util.Check;
import com.github.mouse0w0.peach.javafx.util.NotificationLevel;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Side;
import javafx.scene.Node;

public abstract class ValueField<T> extends Element {
    public static final String WARNING = "warning";
    public static final String ERROR = "error";

    private static final PopupAlert POPUP_ALERT;

    private static final ChangeListener<Boolean> FOCUSED_LISTENER;

    private BooleanProperty editable;

    private ObservableList<Check<? super T>> checks;
    private SortedList<Check<? super T>> sortedChecks;
    private ReadOnlyBooleanWrapper valid;
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

                POPUP_ALERT.setLevel(item.getLevel());
                POPUP_ALERT.setText(String.format(item.getMessage(), element.getValue()));
                POPUP_ALERT.show(bean, Side.TOP, 0, -3);
            } else {
                POPUP_ALERT.hide();
                element.validate();
            }
        };
    }

    private static ValueField<?> getElement(Node node) {
        return node.hasProperties() ? (ValueField<?>) node.getProperties().get(Element.class) : null;
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

    private ReadOnlyBooleanWrapper validPropertyImpl() {
        if (valid == null) {
            valid = new ReadOnlyBooleanWrapper(this, "valid", true);
        }
        return valid;
    }

    public final ReadOnlyBooleanProperty validProperty() {
        return validPropertyImpl().getReadOnlyProperty();
    }

    public final boolean isValid() {
        return valid == null || valid.get();
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

        if (sortedChecks == null) {
            sortedChecks = checks.sorted();
        }

        final T value = getValue();

        boolean valid = true;
        for (Check<? super T> check : sortedChecks) {
            if (!check.test(value)) {
                invalidCheckPropertyImpl().set(check);
                valid = check.getLevel() != NotificationLevel.ERROR;
                break;
            }
        }

        if (valid) {
            invalidCheckPropertyImpl().set(null);
        }
        updateStyleClass();
        validPropertyImpl().set(valid);
        return valid;
    }

    private void updateStyleClass() {
        Check<?> check = getInvalidCheck();
        NotificationLevel level = check == null ? NotificationLevel.NONE : check.getLevel();
        ObservableList<String> styleClass = getEditor().getStyleClass();
        styleClass.removeAll(ERROR, WARNING);
        if (level == NotificationLevel.ERROR) {
            styleClass.add(ERROR);
        } else if (level == NotificationLevel.WARNING) {
            styleClass.add(WARNING);
        }
    }
}
