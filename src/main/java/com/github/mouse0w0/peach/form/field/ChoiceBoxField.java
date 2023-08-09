package com.github.mouse0w0.peach.form.field;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;

public class ChoiceBoxField<T> extends ValueField<T> {
    public ChoiceBoxField() {
        this(FXCollections.observableArrayList());
    }

    public ChoiceBoxField(ObservableList<T> items) {
        setItems(items);
    }

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
        this.value.set(value);
    }

    private final ObjectProperty<ObservableList<T>> items = new SimpleObjectProperty<>(this, "items");

    public final ObjectProperty<ObservableList<T>> itemsProperty() {
        return items;
    }

    public final ObservableList<T> getItems() {
        return items.get();
    }

    public final void setItems(ObservableList<T> value) {
        items.set(value);
    }

    private final ObjectProperty<StringConverter<T>> converter = new SimpleObjectProperty<>(this, "converter");

    public final ObjectProperty<StringConverter<T>> converterProperty() {
        return converter;
    }

    public final StringConverter<T> getConverter() {
        return converter.get();
    }

    public final void setConverter(StringConverter<T> value) {
        converter.set(value);
    }

    @Override
    protected Node createEditorNode() {
        ChoiceBox<T> choiceBox = new ChoiceBox<>(getItems());
        choiceBox.setMaxWidth(Double.MAX_VALUE);
        choiceBox.valueProperty().bindBidirectional(valueProperty());
        choiceBox.itemsProperty().bind(itemsProperty());
        choiceBox.converterProperty().bind(converterProperty());
        choiceBox.disableProperty().bind(disableProperty());
        return choiceBox;
    }
}
