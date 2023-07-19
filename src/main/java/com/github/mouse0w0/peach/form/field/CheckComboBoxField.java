package com.github.mouse0w0.peach.form.field;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.IndexedCheckModel;

import java.util.Collection;

public class CheckComboBoxField<T> extends MultiValueField<T> {

    @Override
    public final ObservableList<T> getValues() {
        return getCheckModel().getCheckedItems();
    }

    @Override
    public final void setValues(Collection<? extends T> collection) {
        final IndexedCheckModel<T> checkModel = getCheckModel();
        for (T item : collection) {
            checkModel.check(item);
        }
    }

    @Override
    @SafeVarargs
    public final void setValues(T... items) {
        final IndexedCheckModel<T> checkModel = getCheckModel();
        for (T item : items) {
            checkModel.check(item);
        }
    }

    public ObservableList<T> getItems() {
        return getCheckComboBox().getItems();
    }

    public BooleanProperty getItemBooleanProperty(int index) {
        return getCheckComboBox().getItemBooleanProperty(index);
    }

    public BooleanProperty getItemBooleanProperty(T item) {
        return getCheckComboBox().getItemBooleanProperty(item);
    }

    public void setCheckModel(IndexedCheckModel<T> value) {
        getCheckComboBox().setCheckModel(value);
    }

    public IndexedCheckModel<T> getCheckModel() {
        return getCheckComboBox().getCheckModel();
    }

    public ObjectProperty<IndexedCheckModel<T>> checkModelProperty() {
        return getCheckComboBox().checkModelProperty();
    }

    public ObjectProperty<StringConverter<T>> converterProperty() {
        return getCheckComboBox().converterProperty();
    }

    public void setConverter(StringConverter<T> value) {
        getCheckComboBox().setConverter(value);
    }

    public StringConverter<T> getConverter() {
        return getCheckComboBox().getConverter();
    }

    public StringProperty titleProperty() {
        return getCheckComboBox().titleProperty();
    }

    public void setTitle(String value) {
        getCheckComboBox().setTitle(value);
    }

    public String getTitle() {
        return getCheckComboBox().getTitle();
    }

    public void setShowCheckedCount(boolean value) {
        getCheckComboBox().setShowCheckedCount(value);
    }

    public boolean isShowCheckedCount() {
        return getCheckComboBox().isShowCheckedCount();
    }

    @SuppressWarnings("unchecked")
    public CheckComboBox<T> getCheckComboBox() {
        return (CheckComboBox<T>) getEditor();
    }

    @Override
    protected Node createEditor() {
        CheckComboBox<T> checkComboBox = new CheckComboBox<>();
        checkComboBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        checkComboBox.disableProperty().bind(disableProperty());
        return checkComboBox;
    }
}
