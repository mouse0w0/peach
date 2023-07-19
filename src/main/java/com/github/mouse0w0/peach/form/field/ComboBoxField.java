package com.github.mouse0w0.peach.form.field;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class ComboBoxField<T> extends ValueField<T> {
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
        return getComboBox().itemsProperty();
    }

    public final ObservableList<T> getItems() {
        return getComboBox().getItems();
    }

    public final void setItems(ObservableList<T> value) {
        getComboBox().setItems(value);
    }

    public final ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactoryProperty() {
        return getComboBox().cellFactoryProperty();
    }

    public final Callback<ListView<T>, ListCell<T>> getCellFactory() {
        return getComboBox().getCellFactory();
    }

    public final void setCellFactory(Callback<ListView<T>, ListCell<T>> value) {
        getComboBox().setCellFactory(value);
    }

    public final ObjectProperty<StringConverter<T>> converterProperty() {
        return getComboBox().converterProperty();
    }

    public final void setConverter(StringConverter<T> value) {
        getComboBox().setConverter(value);
    }

    public final StringConverter<T> getConverter() {
        return getComboBox().getConverter();
    }

    public final ObjectProperty<ListCell<T>> buttonCellProperty() {
        return getComboBox().buttonCellProperty();
    }

    public final ListCell<T> getButtonCell() {
        return getComboBox().getButtonCell();
    }

    public final void setButtonCell(ListCell<T> value) {
        getComboBox().setButtonCell(value);
    }

    public final StringProperty promptTextProperty() {
        return getComboBox().promptTextProperty();
    }

    public final String getPromptText() {
        return getComboBox().getPromptText();
    }

    public final void setPromptText(String value) {
        getComboBox().setPromptText(value);
    }

    @SuppressWarnings("unchecked")
    public final ComboBox<T> getComboBox() {
        return (ComboBox<T>) getEditor();
    }

    @Override
    protected Node createEditor() {
        ComboBox<T> comboBox = new ComboBox<>();
        comboBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        comboBox.valueProperty().bindBidirectional(valueProperty());
        comboBox.disableProperty().bind(disableProperty());
        return comboBox;
    }
}
