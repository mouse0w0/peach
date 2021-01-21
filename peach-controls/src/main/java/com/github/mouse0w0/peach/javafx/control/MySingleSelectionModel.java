package com.github.mouse0w0.peach.javafx.control;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.scene.control.SingleSelectionModel;

import java.util.Objects;

public class MySingleSelectionModel<T> extends SingleSelectionModel<T> {
    private final ObservableList<T> items;

    private final ListChangeListener<T> listChangeListener = new ListChangeListener<T>() {
        @Override
        public void onChanged(Change<? extends T> c) {
            if (getItems().isEmpty()) {
                clearSelection();
            } else {
                final int selectedIndex = getSelectedIndex();
                final T selectedItem = getSelectedItem();
                final T currentItem = getModelItem(selectedIndex);
                if (!Objects.equals(selectedItem, currentItem)) {
                    setSelectedIndex(getItems().indexOf(selectedItem));
                }
            }
        }
    };

    public MySingleSelectionModel(ObservableList<T> items) {
        this.items = items;

        items.addListener(new WeakListChangeListener<>(listChangeListener));
    }

    public final ObservableList<T> getItems() {
        return items;
    }

    @Override
    protected T getModelItem(int index) {
        if (index < 0 || index >= getItemCount()) return null;
        return items.get(index);
    }

    @Override
    protected int getItemCount() {
        return items.size();
    }
}
