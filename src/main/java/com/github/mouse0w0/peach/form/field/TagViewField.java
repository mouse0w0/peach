package com.github.mouse0w0.peach.form.field;

import com.github.mouse0w0.peach.javafx.control.TagCell;
import com.github.mouse0w0.peach.javafx.control.TagView;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

public class TagViewField<T> extends MultiValueField<T> {

    @Override
    public final ObservableList<T> getValues() {
        return getTagView().getItems();
    }

    @Override
    public final void setValues(Collection<? extends T> items) {
        getTagView().getItems().setAll(items);
    }

    @Override
    @SafeVarargs
    public final void setValues(T... items) {
        getTagView().getItems().setAll(items);
    }

    public ObjectProperty<Supplier<T>> itemFactoryProperty() {
        return getTagView().itemFactoryProperty();
    }

    public Supplier<T> getItemFactory() {
        return getTagView().getItemFactory();
    }

    public void setItemFactory(Supplier<T> itemFactory) {
        getTagView().setItemFactory(itemFactory);
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
