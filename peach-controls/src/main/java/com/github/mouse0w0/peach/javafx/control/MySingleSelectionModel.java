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
                return;
            }

            int oldIndex = getSelectedIndex();
            if (oldIndex == -1) {
                if (getSelectedItem() != null) {
                    int newIndex = getItems().indexOf(getSelectedItem());
                    if (newIndex != -1) {
                        setSelectedIndex(newIndex);
                    }
                }
                return;
            }

            int newIndex = oldIndex;
            while (c.next()) {
                if (c.getFrom() <= newIndex && newIndex != -1) {
                    if (c.wasAdded()) {
                        newIndex += c.getAddedSize();
                    } else if (c.wasRemoved()) {
                        newIndex -= c.getRemovedSize();
                    }
                }
            }

            if (oldIndex != newIndex) {
                final T selectedItem = getSelectedItem();
                final T currentItem = getModelItem(newIndex);
                if (Objects.equals(selectedItem, currentItem)) {
                    clearAndSelect(newIndex);
                } else {
                    select(selectedItem);
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
