package com.github.mouse0w0.peach.javafx.control;

import com.github.mouse0w0.peach.javafx.control.skin.TagCellSkin;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.Skin;

public class TagCell<T> extends IndexedCell<T> {
    private final InvalidationListener selectedIndexListener = observable -> updateSelection();

    public TagCell() {
        getStyleClass().add("tag-cell");

        visibleProperty().bind(emptyProperty().not());
        managedProperty().bind(emptyProperty().not());

        tagView.addListener(observable -> {
            SelectionModel<T> sm = getTagView().getSelectionModel();
            sm.selectedIndexProperty().addListener(new WeakInvalidationListener(selectedIndexListener));
        });
    }

    private final ReadOnlyObjectWrapper<TagView<T>> tagView = new ReadOnlyObjectWrapper<>(this, "tagView");

    public final ReadOnlyObjectProperty<TagView<T>> tagViewProperty() {
        return tagView.getReadOnlyProperty();
    }

    public final TagView<T> getTagView() {
        return tagView.get();
    }

    private void setTagView(TagView<T> tagView) {
        this.tagView.set(tagView);
    }

    public final void updateTagView(TagView<T> tagView) {
        setTagView(tagView);
    }

    @Override
    public void updateIndex(int i) {
        int oldIndex = getIndex();
        super.updateIndex(i);
        updateItem(oldIndex);
        updateSelection();
    }

    public void updateItem(int oldIndex) {
        TagView<T> tagView = getTagView();
        if (tagView != null) {
            ObservableList<T> items = tagView.getItems();
            int newIndex = getIndex();
            if (newIndex >= 0 && newIndex < items.size()) {
                T oldItem = getItem();
                T newItem = items.get(newIndex);
                if (oldIndex != newIndex || isItemChanged(oldItem, newItem)) {
                    updateItem(newItem, newItem == null);
                }
                return;
            }
        }
        updateItem(null, true);
    }

    private void updateSelection() {
        if (isEmpty()) return;

        final int index = getIndex();
        if (index == -1) return;

        final TagView<T> tagView = getTagView();
        if (tagView == null) return;

        final SelectionModel<T> selectionModel = tagView.getSelectionModel();

        final boolean selected = selectionModel.isSelected(index);
        if (isSelected() != selected) {
            updateSelected(selected);
        }
    }

    @Override
    public void startEdit() {
        if (isEditing()) return;

        super.startEdit();

        if (isEditing()) {
            TagView<T> tagView = getTagView();
            if (tagView != null) {
                tagView.fireEvent(new TagView.TagEvent<>(tagView, TagView.editStartEvent(), getIndex(), getItem()));
                tagView.edit(getIndex());
            }
        }
    }

    @Override
    public void commitEdit(T newValue) {
        if (!isEditing()) return;

        TagView<T> tagView = getTagView();
        if (tagView != null) {
            tagView.fireEvent(new TagView.TagEvent<>(tagView, TagView.editCommitEvent(), getIndex(), newValue));
        }

        super.commitEdit(newValue);

        if (tagView != null) {
            tagView.edit(-1);
        }
    }

    @Override
    public void cancelEdit() {
        if (!isEditing()) return;

        TagView<T> tagView = getTagView();
        if (tagView != null) {
            tagView.fireEvent(new TagView.TagEvent<>(tagView, TagView.editCancelEvent(), getIndex(), getItem()));
        }

        super.cancelEdit();

        if (tagView != null) {
            tagView.edit(-1);
        }
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TagCellSkin<>(this);
    }
}
