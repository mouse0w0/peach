package com.github.mouse0w0.peach.form.field;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SingleSelectionModel;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class ComboBoxField<T> extends ValueField<T> {
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
        return getComboBox().itemsProperty();
    }

    public ObservableList<T> getItems() {
        return getComboBox().getItems();
    }

    public void setItems(ObservableList<T> value) {
        getComboBox().setItems(value);
    }

    public ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactoryProperty() {
        return getComboBox().cellFactoryProperty();
    }

    public Callback<ListView<T>, ListCell<T>> getCellFactory() {
        return getComboBox().getCellFactory();
    }

    public void setCellFactory(Callback<ListView<T>, ListCell<T>> value) {
        getComboBox().setCellFactory(value);
    }

    public ObjectProperty<StringConverter<T>> converterProperty() {
        return getComboBox().converterProperty();
    }

    public void setConverter(StringConverter<T> value) {
        getComboBox().setConverter(value);
    }

    public StringConverter<T> getConverter() {
        return getComboBox().getConverter();
    }

    public ObjectProperty<ListCell<T>> buttonCellProperty() {
        return getComboBox().buttonCellProperty();
    }

    public ListCell<T> getButtonCell() {
        return getComboBox().getButtonCell();
    }

    public void setButtonCell(ListCell<T> value) {
        getComboBox().setButtonCell(value);
    }

    public ObjectProperty<SingleSelectionModel<T>> selectionModelProperty() {
        return getComboBox().selectionModelProperty();
    }

    public SingleSelectionModel<T> getSelectionModel() {
        return getComboBox().getSelectionModel();
    }

    public void setSelectionModel(SingleSelectionModel<T> value) {
        getComboBox().setSelectionModel(value);
    }

    @SuppressWarnings("unchecked")
    public ComboBox<T> getComboBox() {
        return (ComboBox<T>) getEditor();
    }

    @Override
    protected Node createDefaultEditor() {
        ComboBox<T> comboBox = new ComboBox<>();
        comboBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        comboBox.valueProperty().bindBidirectional(valueProperty());
        comboBox.promptTextProperty().bind(promptTextProperty());
        comboBox.disableProperty().bind(disableProperty());
        return comboBox;
    }
}
