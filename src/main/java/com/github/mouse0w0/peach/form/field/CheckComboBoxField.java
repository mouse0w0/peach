package com.github.mouse0w0.peach.form.field;

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
    public final void setValues(T... elements) {
        final IndexedCheckModel<T> checkModel = getCheckModel();
        for (T element : elements) {
            checkModel.check(element);
        }
    }

    public final ObservableList<T> getItems() {
        return getCheckComboBox().getItems();
    }

    public final ObjectProperty<IndexedCheckModel<T>> checkModelProperty() {
        return getCheckComboBox().checkModelProperty();
    }

    public final IndexedCheckModel<T> getCheckModel() {
        return getCheckComboBox().getCheckModel();
    }

    public final void setCheckModel(IndexedCheckModel<T> value) {
        getCheckComboBox().setCheckModel(value);
    }

    public final ObjectProperty<StringConverter<T>> converterProperty() {
        return getCheckComboBox().converterProperty();
    }

    public final void setConverter(StringConverter<T> value) {
        getCheckComboBox().setConverter(value);
    }

    public final StringConverter<T> getConverter() {
        return getCheckComboBox().getConverter();
    }

    public final StringProperty titleProperty() {
        return getCheckComboBox().titleProperty();
    }

    public final void setTitle(String value) {
        getCheckComboBox().setTitle(value);
    }

    public final String getTitle() {
        return getCheckComboBox().getTitle();
    }

    public final void setShowCheckedCount(boolean value) {
        getCheckComboBox().setShowCheckedCount(value);
    }

    public final boolean isShowCheckedCount() {
        return getCheckComboBox().isShowCheckedCount();
    }

    @SuppressWarnings("unchecked")
    public final CheckComboBox<T> getCheckComboBox() {
        return (CheckComboBox<T>) getEditorNode();
    }

    @Override
    protected Node createEditorNode() {
        CheckComboBox<T> checkComboBox = new CheckComboBox<>();
        checkComboBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        checkComboBox.disableProperty().bind(disableProperty());
        return checkComboBox;
    }
}
