package com.github.mouse0w0.peach.form.field;

import com.github.mouse0w0.peach.javafx.control.TagCell;
import com.github.mouse0w0.peach.javafx.control.TagView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;

import java.util.Collection;
import java.util.function.Function;

public class TagViewField<T> extends Field {

    public ObservableList<T> getItems() {
        return getTagView().getItems();
    }

    public final void setItems(Collection<? extends T> items) {
        getTagView().getItems().setAll(items);
    }

    @SafeVarargs
    public final void setItems(T... items) {
        getTagView().getItems().setAll(items);
    }

    public ReadOnlyIntegerProperty editingIndexProperty() {
        return getTagView().editingIndexProperty();
    }

    public int getEditingIndex() {
        return getTagView().getEditingIndex();
    }

    public void edit(int index) {
        getTagView().edit(index);
    }

    public ObjectProperty<Function<TagView<T>, TagCell<T>>> cellFactoryProperty() {
        return getTagView().cellFactoryProperty();
    }

    public Function<TagView<T>, TagCell<T>> getCellFactory() {
        return getTagView().getCellFactory();
    }

    public void setCellFactory(Function<TagView<T>, TagCell<T>> cellFactory) {
        getTagView().setCellFactory(cellFactory);
    }

    public ObjectProperty<EventHandler<TagView.TagEvent<T>>> onAddProperty() {
        return getTagView().onAddProperty();
    }

    public EventHandler<TagView.TagEvent<T>> getOnAdd() {
        return getTagView().getOnAdd();
    }

    public void setOnAdd(EventHandler<TagView.TagEvent<T>> onAdd) {
        getTagView().setOnAdd(onAdd);
    }

    public ObjectProperty<EventHandler<TagView.TagEvent<T>>> onRemoveProperty() {
        return getTagView().onRemoveProperty();
    }

    public EventHandler<TagView.TagEvent<T>> getOnRemove() {
        return getTagView().getOnRemove();
    }

    public void setOnRemove(EventHandler<TagView.TagEvent<T>> onRemove) {
        getTagView().setOnRemove(onRemove);
    }

    public ObjectProperty<EventHandler<TagView.TagEvent<T>>> onEditStartProperty() {
        return getTagView().onEditStartProperty();
    }

    public EventHandler<TagView.TagEvent<T>> getOnEditStart() {
        return getTagView().getOnEditStart();
    }

    public void setOnEditStart(EventHandler<TagView.TagEvent<T>> onEditStart) {
        getTagView().setOnEditStart(onEditStart);
    }

    public ObjectProperty<EventHandler<TagView.TagEvent<T>>> onEditCommitProperty() {
        return getTagView().onEditCommitProperty();
    }

    public EventHandler<TagView.TagEvent<T>> getOnEditCommit() {
        return getTagView().getOnEditCommit();
    }

    public void setOnEditCommit(EventHandler<TagView.TagEvent<T>> onEditCommit) {
        getTagView().setOnEditCommit(onEditCommit);
    }

    public ObjectProperty<EventHandler<TagView.TagEvent<T>>> onEditCancelProperty() {
        return getTagView().onEditCancelProperty();
    }

    public EventHandler<TagView.TagEvent<T>> getOnEditCancel() {
        return getTagView().getOnEditCancel();
    }

    public void setOnEditCancel(EventHandler<TagView.TagEvent<T>> onEditCancel) {
        getTagView().setOnEditCancel(onEditCancel);
    }

    @SuppressWarnings("unchecked")
    public TagView<T> getTagView() {
        return (TagView<T>) getEditor();
    }

    @Override
    protected Node createEditor() {
        TagView<T> tagView = new TagView<>();
        tagView.disableProperty().bind(disableProperty());
        return tagView;
    }
}
