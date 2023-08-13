package com.github.mouse0w0.peach.ui.control;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.scene.control.FocusModel;

public class MyFocusModel<T> extends FocusModel<T> {
    private ObservableList<T> items;

    private final ListChangeListener<T> itemsChangeListener = new ListChangeListener<>() {
        @Override
        public void onChanged(Change<? extends T> c) {
            while (c.next()) {
                int from = c.getFrom();
                if (getFocusedIndex() == -1 || from > getFocusedIndex()) {
                    return;
                }

                c.reset();
                boolean added = false;
                boolean removed = false;
                int addedSize = 0;
                int removedSize = 0;
                while (c.next()) {
                    added |= c.wasAdded();
                    removed |= c.wasRemoved();
                    addedSize += c.getAddedSize();
                    removedSize += c.getRemovedSize();
                }

                if (added && !removed) {
                    focus(Math.min(getItemCount() - 1, getFocusedIndex() + addedSize));
                } else if (!added && removed) {
                    focus(Math.max(0, getFocusedIndex() - removedSize));
                }
            }
        }
    };
    private final WeakListChangeListener<T> weakItemsChangeListener = new WeakListChangeListener<>(itemsChangeListener);

    public MyFocusModel(ObservableList<T> items) {
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
        int newFocusIndex = -1;
        if (items != null) {
            items.addListener(weakItemsChangeListener);
            T selectedItem = getFocusedItem();
            if (selectedItem != null) {
                newFocusIndex = items.indexOf(selectedItem);
            }
        }
        focus(newFocusIndex);
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
