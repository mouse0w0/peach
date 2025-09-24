package com.github.mouse0w0.peach.ui.control;

import javafx.collections.ObservableList;
import org.controlsfx.control.IndexedCheckModel;

public class CheckComboBox<T> extends org.controlsfx.control.CheckComboBox<T> {
    public CheckComboBox() {
    }

    public CheckComboBox(ObservableList<T> items) {
        super(items);
    }

    public final ObservableList<T> getCheckedItems() {
        return getCheckModel().getCheckedItems();
    }

    public final void clearCheck(T item) {
        getCheckModel().clearCheck(item);
    }

    public final void clearChecks() {
        getCheckModel().clearChecks();
    }

    public final void checkAll() {
        getCheckModel().checkAll();
    }

    public final boolean isChecked(T item) {
        return getCheckModel().isChecked(item);
    }

    public final void check(T item) {
        getCheckModel().check(item);
    }

    public final void toggleCheckState(T item) {
        getCheckModel().toggleCheckState(item);
    }

    public final void toggleCheckState(int index) {
        getCheckModel().toggleCheckState(index);
    }

    public final void clearCheck(int index) {
        getCheckModel().clearCheck(index);
    }

    public final void checkIndices(int... indices) {
        getCheckModel().checkIndices(indices);
    }

    @SafeVarargs
    public final void checkItems(T... items) {
        IndexedCheckModel<T> checkModel = getCheckModel();
        for (T item : items) {
            checkModel.check(item);
        }
    }

    public final void checkItems(Iterable<T> items) {
        IndexedCheckModel<T> checkModel = getCheckModel();
        for (T item : items) {
            checkModel.check(item);
        }
    }

    public final boolean isChecked(int index) {
        return getCheckModel().isChecked(index);
    }

    public final void check(int index) {
        getCheckModel().check(index);
    }

    public final ObservableList<Integer> getCheckedIndices() {
        return getCheckModel().getCheckedIndices();
    }
}
