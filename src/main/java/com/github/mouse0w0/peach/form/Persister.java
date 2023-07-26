package com.github.mouse0w0.peach.form;

import com.github.mouse0w0.peach.javafx.util.WritableValueWrapper;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.WritableValue;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class Persister {
    private final List<PersisterEntry> persisterEntryList = new ArrayList<>();

    public <T> void register(@NotNull Property<T> property, @NotNull Supplier<T> persistentGetter, @NotNull Consumer<T> persistentSetter) {
        persisterEntryList.add(new PropertyPersisterEntry<>(property, new WritableValueWrapper<>(persistentGetter, persistentSetter)));
    }

    public <T> void register(@NotNull Property<T> property, @NotNull WritableValue<T> persistentValue) {
        persisterEntryList.add(new PropertyPersisterEntry<>(property, persistentValue));
    }

    public <T> void register(@NotNull ObservableList<T> observableList, @NotNull List<T> persistentList) {
        persisterEntryList.add(new ListPersisterEntry<>(observableList, persistentList));
    }

    private final BooleanProperty modified = new SimpleBooleanProperty(this, "modified");

    public ReadOnlyBooleanProperty modifiedProperty() {
        return modified;
    }

    public boolean isModified() {
        return modified.get();
    }

    private int modifiedCount;

    private void notifyModification(boolean modified) {
        if (modified) {
            modifiedCount++;
            if (modifiedCount > 0) {
                this.modified.set(true);
            }
        } else {
            modifiedCount--;
            if (modifiedCount <= 0) {
                this.modified.set(false);
            }
        }
    }

    public void persist() {
        for (PersisterEntry entry : persisterEntryList) {
            entry.persist();
        }
    }

    public void reset() {
        for (PersisterEntry entry : persisterEntryList) {
            entry.reset();
        }
    }

    private abstract class PersisterEntry {
        private boolean modified;

        void checkModification() {
            boolean newModified = isModified();
            if (modified != newModified) {
                modified = newModified;
                notifyModification(newModified);
            }
        }

        abstract boolean isModified();

        abstract void persist();

        abstract void reset();
    }

    private final class PropertyPersisterEntry<T> extends PersisterEntry {
        private final Property<T> property;
        private final WritableValue<T> persistentValue;

        public PropertyPersisterEntry(Property<T> property, WritableValue<T> persistentValue) {
            this.property = property;
            this.persistentValue = persistentValue;
            property.addListener(new Invalidator(this));
            checkModification();
        }

        boolean isModified() {
            return !Objects.equals(property.getValue(), persistentValue.getValue());
        }

        @Override
        void persist() {
            persistentValue.setValue(property.getValue());
        }

        @Override
        void reset() {
            property.setValue(persistentValue.getValue());
        }
    }

    private final class ListPersisterEntry<T> extends PersisterEntry {
        private final ObservableList<T> observableList;
        private final List<T> persistentList;

        public ListPersisterEntry(ObservableList<T> observableList, List<T> persistentList) {
            this.observableList = observableList;
            this.persistentList = persistentList;
            observableList.addListener(new Invalidator(this));
            checkModification();
        }

        boolean isModified() {
            return !observableList.equals(persistentList);
        }

        @Override
        void persist() {
            persistentList.clear();
            persistentList.addAll(observableList);
        }

        @Override
        void reset() {
            observableList.setAll(persistentList);
        }
    }

    private final static class Invalidator implements InvalidationListener, WeakListener {
        private final WeakReference<PersisterEntry> ref;

        public Invalidator(PersisterEntry persisterEntry) {
            this.ref = new WeakReference<>(persisterEntry);
        }

        @Override
        public void invalidated(Observable observable) {
            PersisterEntry persisterEntry = ref.get();
            if (persisterEntry != null) {
                persisterEntry.checkModification();
            } else {
                observable.removeListener(this);
            }
        }

        @Override
        public boolean wasGarbageCollected() {
            return ref.get() == null;
        }
    }
}
