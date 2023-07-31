package com.github.mouse0w0.peach.form.field;

import com.github.mouse0w0.peach.javafx.control.TagCell;
import com.github.mouse0w0.peach.javafx.control.TagView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

public class TagViewField<T> extends MultiValueField<T> {

    private final ObservableList<T> values = FXCollections.observableArrayList();

    @Override
    public final ObservableList<T> getValues() {
        return values;
    }

    @Override
    public final void setValues(Collection<? extends T> items) {
        values.setAll(items);
    }

    @Override
    @SafeVarargs
    public final void setValues(T... items) {
        values.setAll(items);
    }

    private final ObjectProperty<Supplier<T>> itemFactory = new SimpleObjectProperty<>(this, "itemFactory");

    public final ObjectProperty<Supplier<T>> itemFactoryProperty() {
        return itemFactory;
    }

    public final Supplier<T> getItemFactory() {
        return itemFactory.get();
    }

    public final void setItemFactory(Supplier<T> value) {
        itemFactory.set(value);
    }

    private final ObjectProperty<Function<TagView<T>, TagCell<T>>> cellFactory = new SimpleObjectProperty<>(this, "cellFactory");

    public final ObjectProperty<Function<TagView<T>, TagCell<T>>> cellFactoryProperty() {
        return cellFactory;
    }

    public final Function<TagView<T>, TagCell<T>> getCellFactory() {
        return cellFactory.get();
    }

    public final void setCellFactory(Function<TagView<T>, TagCell<T>> value) {
        cellFactory.set(value);
    }

    @Override
    protected Node createEditor() {
        TagView<T> tagView = new TagView<>();
        Bindings.bindContentBidirectional(tagView.getItems(), values);
        tagView.itemFactoryProperty().bind(itemFactoryProperty());
        tagView.cellFactoryProperty().bind(cellFactoryProperty());
        tagView.disableProperty().bind(disableProperty());
        return tagView;
    }
}
