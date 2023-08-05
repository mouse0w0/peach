package com.github.mouse0w0.peach.javafx.control;

import com.github.mouse0w0.peach.javafx.control.skin.TagViewSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.Skin;

import java.util.function.Function;
import java.util.function.Supplier;

public class TagView<T> extends Control {
    @SuppressWarnings("unchecked")
    public static <T> EventType<TagEvent<T>> anyEvent() {
        return (EventType<TagEvent<T>>) ANY_EVENT;
    }

    private static final EventType<?> ANY_EVENT = new EventType<>(Event.ANY, "TAG_VIEW");

    @SuppressWarnings("unchecked")
    public static <T> EventType<TagEvent<T>> addEvent() {
        return (EventType<TagEvent<T>>) ADD_EVENT;
    }

    private static final EventType<?> ADD_EVENT = new EventType<>(anyEvent(), "ADD");

    @SuppressWarnings("unchecked")
    public static <T> EventType<TagEvent<T>> removeEvent() {
        return (EventType<TagEvent<T>>) REMOVE_EVENT;
    }

    private static final EventType<?> REMOVE_EVENT = new EventType<>(anyEvent(), "REMOVE");

    public static <T> EventType<TagEvent<T>> editStartEvent() {
        return (EventType<TagEvent<T>>) EDIT_START_EVENT;
    }

    private static final EventType<?> EDIT_START_EVENT =
            new EventType<>(anyEvent(), "EDIT_START");

    @SuppressWarnings("unchecked")
    public static <T> EventType<TagEvent<T>> editCancelEvent() {
        return (EventType<TagEvent<T>>) EDIT_CANCEL_EVENT;
    }

    private static final EventType<?> EDIT_CANCEL_EVENT =
            new EventType<>(anyEvent(), "EDIT_CANCEL");

    @SuppressWarnings("unchecked")
    public static <T> EventType<TagEvent<T>> editCommitEvent() {
        return (EventType<TagEvent<T>>) EDIT_COMMIT_EVENT;
    }

    private static final EventType<?> EDIT_COMMIT_EVENT =
            new EventType<>(anyEvent(), "EDIT_COMMIT");

    public TagView() {
        getStyleClass().add("tag-view");

        setOnAdd(event -> {
            Supplier<T> itemFactory = getItemFactory();
            if (itemFactory != null) {
                getItems().add(event.getIndex(), itemFactory.get());
                edit(event.getIndex());
            }
        });
        setOnRemove(event -> getItems().remove(event.getIndex()));
        setOnEditCommit(event -> getItems().set(event.getIndex(), event.getValue()));
    }

    private final ObservableList<T> items = FXCollections.observableArrayList();

    public final ObservableList<T> getItems() {
        return items;
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
    ;

    public final ObjectProperty<Function<TagView<T>, TagCell<T>>> cellFactoryProperty() {
        return cellFactory;
    }

    public final Function<TagView<T>, TagCell<T>> getCellFactory() {
        return cellFactory.get();
    }

    public final void setCellFactory(Function<TagView<T>, TagCell<T>> value) {
        cellFactory.set(value);
    }

    private final SelectionModel<T> selectionModel = new MySingleSelectionModel<>(items);

    public final SelectionModel<T> getSelectionModel() {
        return selectionModel;
    }

    private ReadOnlyIntegerWrapper editingIndex;

    private void setEditingIndex(int value) {
        editingIndexPropertyImpl().set(value);
    }

    public final int getEditingIndex() {
        return editingIndex == null ? -1 : editingIndex.get();
    }

    public final ReadOnlyIntegerProperty editingIndexProperty() {
        return editingIndexPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyIntegerWrapper editingIndexPropertyImpl() {
        if (editingIndex == null) {
            editingIndex = new ReadOnlyIntegerWrapper(this, "editingIndex", -1);
        }
        return editingIndex;
    }

    public void edit(int index) {
        setEditingIndex(index);
    }

    private ObjectProperty<EventHandler<TagEvent<T>>> onAdd;

    public final ObjectProperty<EventHandler<TagEvent<T>>> onAddProperty() {
        if (onAdd == null) {
            onAdd = new SimpleObjectProperty<>(this, "onAdd") {
                @Override
                protected void invalidated() {
                    setEventHandler(addEvent(), get());
                }
            };
        }
        return onAdd;
    }

    public final EventHandler<TagEvent<T>> getOnAdd() {
        return onAdd != null ? onAdd.get() : null;
    }

    public final void setOnAdd(EventHandler<TagEvent<T>> onAdd) {
        onAddProperty().set(onAdd);
    }

    private ObjectProperty<EventHandler<TagEvent<T>>> onRemove;

    public final ObjectProperty<EventHandler<TagEvent<T>>> onRemoveProperty() {
        if (onRemove == null) {
            onRemove = new SimpleObjectProperty<>(this, "onRemove") {
                @Override
                protected void invalidated() {
                    setEventHandler(removeEvent(), get());
                }
            };
        }
        return onRemove;
    }

    public final EventHandler<TagEvent<T>> getOnRemove() {
        return onRemove != null ? onRemove.get() : null;
    }

    public final void setOnRemove(EventHandler<TagEvent<T>> onRemove) {
        onRemoveProperty().set(onRemove);
    }

    private ObjectProperty<EventHandler<TagEvent<T>>> onEditStart;

    public final ObjectProperty<EventHandler<TagEvent<T>>> onEditStartProperty() {
        if (onEditStart == null) {
            onEditStart = new SimpleObjectProperty<>(this, "onEditStart") {
                @Override
                protected void invalidated() {
                    setEventHandler(editStartEvent(), get());
                }
            };
        }
        return onEditStart;
    }

    public final EventHandler<TagEvent<T>> getOnEditStart() {
        return onEditStart != null ? onEditStart.get() : null;
    }

    public final void setOnEditStart(EventHandler<TagEvent<T>> onEditStart) {
        onEditStartProperty().set(onEditStart);
    }

    private ObjectProperty<EventHandler<TagEvent<T>>> onEditCommit;

    public final ObjectProperty<EventHandler<TagEvent<T>>> onEditCommitProperty() {
        if (onEditCommit == null) {
            onEditCommit = new SimpleObjectProperty<>(this, "onEditCommit") {
                @Override
                protected void invalidated() {
                    setEventHandler(editCommitEvent(), get());
                }
            };
        }
        return onEditCommit;
    }

    public final EventHandler<TagEvent<T>> getOnEditCommit() {
        return onEditCommit != null ? onEditCommit.get() : null;
    }

    public final void setOnEditCommit(EventHandler<TagEvent<T>> onEditCommit) {
        onEditCommitProperty().set(onEditCommit);
    }

    private ObjectProperty<EventHandler<TagEvent<T>>> onEditCancel;

    public final ObjectProperty<EventHandler<TagEvent<T>>> onEditCancelProperty() {
        if (onEditCancel == null) {
            onEditCancel = new SimpleObjectProperty<>(this, "onEditCancel") {
                @Override
                protected void invalidated() {
                    setEventHandler(editCancelEvent(), get());
                }
            };
        }
        return onEditCancel;
    }

    public final EventHandler<TagEvent<T>> getOnEditCancel() {
        return onEditCancel != null ? onEditCancel.get() : null;
    }

    public final void setOnEditCancel(EventHandler<TagEvent<T>> onEditCancel) {
        onEditCancelProperty().set(onEditCancel);
    }

    @SuppressWarnings("unchecked")
    public final Node getAddButton() {
        return ((TagViewSkin<T>) getSkin()).getAddButton();
    }

    @Override
    public Orientation getContentBias() {
        return Orientation.HORIZONTAL;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TagViewSkin<>(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return TagView.class.getResource("TagView.css").toExternalForm();
    }

    public static class TagEvent<T> extends Event {

        private final int index;

        private T value;

        public TagEvent(EventTarget source, EventType<? extends TagEvent> eventType, int index, T value) {
            this(source, eventType, index);
            setValue(value);
        }

        public TagEvent(EventTarget source, EventType<? extends TagEvent> eventType, int index) {
            super(source, Event.NULL_SOURCE_TARGET, eventType);
            this.index = index;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public int getIndex() {
            return index;
        }

        @Override
        public String toString() {
            return "TagEvent{" +
                    "index=" + index +
                    ", value=" + value +
                    '}';
        }
    }
}
