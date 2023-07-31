package com.github.mouse0w0.peach.form.field;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ComboBoxField<T> extends ValueField<T> {
    public ComboBoxField() {
        this(FXCollections.observableArrayList());
    }

    public ComboBoxField(ObservableList<T> items) {
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

    private final ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactory = new SimpleObjectProperty<>(this, "cellFactory");

    public final ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactoryProperty() {
        return cellFactory;
    }

    public final Callback<ListView<T>, ListCell<T>> getCellFactory() {
        return cellFactory.get();
    }

    public final void setCellFactory(Callback<ListView<T>, ListCell<T>> value) {
        cellFactory.set(value);
    }

    private final ObjectProperty<ListCell<T>> buttonCell = new SimpleObjectProperty<>(this, "buttonCell");

    public final ObjectProperty<ListCell<T>> buttonCellProperty() {
        return buttonCell;
    }

    public final ListCell<T> getButtonCell() {
        return buttonCell.get();
    }

    public final void setButtonCell(ListCell<T> value) {
        buttonCell.set(value);
    }

    @Override
    protected Node createEditor() {
        ComboBox<T> comboBox = new ComboBox<>(getItems());
        comboBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        comboBox.valueProperty().bindBidirectional(valueProperty());
        comboBox.itemsProperty().bind(itemsProperty());
        comboBox.cellFactoryProperty().bind(cellFactoryProperty());
        comboBox.buttonCellProperty().bind(buttonCellProperty());
        comboBox.disableProperty().bind(disableProperty());
        return comboBox;
    }
}
