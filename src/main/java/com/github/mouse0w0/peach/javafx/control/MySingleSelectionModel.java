package com.github.mouse0w0.peach.javafx.control;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.scene.control.SingleSelectionModel;

import java.util.Objects;

public class MySingleSelectionModel<T> extends SingleSelectionModel<T> {
    private ObservableList<T> items;

    private final ListChangeListener<T> itemsChangeListener = new ListChangeListener<>() {
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
    private final WeakListChangeListener<T> weakItemsChangeListener = new WeakListChangeListener<>(itemsChangeListener);

    public MySingleSelectionModel(ObservableList<T> items) {
        setItems(items);
    }

    public final ObservableList<T> getItems() {
        return items;
    }

    public final void setItems(final ObservableList<T> items) {
        final ObservableList<T> oldItems = this.items;
        if (oldItems != null) {
            oldItems.removeListener(weakItemsChangeListener);
        }
        this.items = items;
        int newSelectedIndex = -1;
        if (items != null) {
            items.addListener(weakItemsChangeListener);
            T selectedItem = getSelectedItem();
            if (selectedItem != null) {
                newSelectedIndex = items.indexOf(selectedItem);
            }
        }
        setSelectedIndex(newSelectedIndex);
    }

    @Override
    protected T getModelItem(int index) {
        final ObservableList<T> items = this.items;
        if (items == null) return null;
        if (index < 0 || index >= items.size()) return null;
        return items.get(index);
    }

    @Override
    protected int getItemCount() {
        final ObservableList<T> items = this.items;
        return items == null ? 0 : items.size();
    }
}
