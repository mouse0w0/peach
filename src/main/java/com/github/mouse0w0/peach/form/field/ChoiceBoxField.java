package com.github.mouse0w0.peach.form.field;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;

public class ChoiceBoxField<T> extends ValueField<T> {
    private final ObjectProperty<T> value = new SimpleObjectProperty<>(this, "value");

    @Override
    public final ObjectProperty<T> valueProperty() {
        return value;
    }

    @Override
    public final T getValue() {
        return value.get();
    }

    @Override
    public final void setValue(T value) {
        valueProperty().setValue(value);
    }

    public final ObjectProperty<ObservableList<T>> itemsProperty() {
        return getChoiceBox().itemsProperty();
    }

    public final ObservableList<T> getItems() {
        return getChoiceBox().getItems();
    }

    public final void setItems(ObservableList<T> value) {
        getChoiceBox().setItems(value);
    }

    public final ObjectProperty<StringConverter<T>> converterProperty() {
        return getChoiceBox().converterProperty();
    }

    public final void setConverter(StringConverter<T> value) {
        getChoiceBox().setConverter(value);
    }

    public final StringConverter<T> getConverter() {
        return getChoiceBox().getConverter();
    }

    @SuppressWarnings("unchecked")
    public final ChoiceBox<T> getChoiceBox() {
        return (ChoiceBox<T>) getEditor();
    }

    @Override
    protected Node createEditor() {
        ChoiceBox<T> choiceBox = new ChoiceBox<>();
        choiceBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        choiceBox.valueProperty().bindBidirectional(valueProperty());
        choiceBox.disableProperty().bind(disableProperty());
        return choiceBox;
    }
}
