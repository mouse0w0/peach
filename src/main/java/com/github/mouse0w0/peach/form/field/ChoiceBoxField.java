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
    public ObjectProperty<T> valueProperty() {
        return value;
    }

    @Override
    public T getValue() {
        return value.get();
    }

    @Override
    public void setValue(T value) {
        valueProperty().setValue(value);
    }

    public ObjectProperty<ObservableList<T>> itemsProperty() {
        return getChoiceBox().itemsProperty();
    }

    public ObservableList<T> getItems() {
        return getChoiceBox().getItems();
    }

    public void setItems(ObservableList<T> value) {
        getChoiceBox().setItems(value);
    }

    public ObjectProperty<StringConverter<T>> converterProperty() {
        return getChoiceBox().converterProperty();
    }

    public void setConverter(StringConverter<T> value) {
        getChoiceBox().setConverter(value);
    }

    public StringConverter<T> getConverter() {
        return getChoiceBox().getConverter();
    }

    @SuppressWarnings("unchecked")
    public ChoiceBox<T> getChoiceBox() {
        return (ChoiceBox<T>) getEditor();
    }

    @Override
    protected Node createDefaultEditor() {
        ChoiceBox<T> choiceBox = new ChoiceBox<>();
        choiceBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        choiceBox.valueProperty().bindBidirectional(valueProperty());
        choiceBox.disableProperty().bind(disableProperty());
        return choiceBox;
    }
}
