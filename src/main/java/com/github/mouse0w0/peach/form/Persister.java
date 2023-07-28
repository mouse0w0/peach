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
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.*;
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

    public <T> void register(@NotNull ObservableSet<T> observableSet, @NotNull Set<T> persistentSet) {
        persisterEntryList.add(new SetPersisterEntry<>(observableSet, persistentSet));
    }

    public <K, V> void register(@NotNull ObservableMap<K, V> observableMap, @NotNull Map<K, V> persistentMap) {
        persisterEntryList.add(new MapPersisterEntry<>(observableMap, persistentMap));
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

    private final class SetPersisterEntry<T> extends PersisterEntry {
        private final ObservableSet<T> observableSet;
        private final Set<T> persistentSet;

        public SetPersisterEntry(ObservableSet<T> observableSet, Set<T> persistentSet) {
            this.observableSet = observableSet;
            this.persistentSet = persistentSet;
            observableSet.addListener(new Invalidator(this));
            checkModification();
        }

        boolean isModified() {
            return !observableSet.equals(persistentSet);
        }

        @Override
        void persist() {
            persistentSet.clear();
            persistentSet.addAll(observableSet);
        }

        @Override
        void reset() {
            observableSet.clear();
            observableSet.addAll(persistentSet);
        }
    }

    private final class MapPersisterEntry<K, V> extends PersisterEntry {
        private final ObservableMap<K, V> observableMap;
        private final Map<K, V> persistentMap;

        public MapPersisterEntry(ObservableMap<K, V> observableMap, Map<K, V> persistentMap) {
            this.observableMap = observableMap;
            this.persistentMap = persistentMap;
            observableMap.addListener(new Invalidator(this));
            checkModification();
        }

        boolean isModified() {
            return !observableMap.equals(persistentMap);
        }

        @Override
        void persist() {
            persistentMap.clear();
            persistentMap.putAll(observableMap);
        }

        @Override
        void reset() {
            observableMap.clear();
            observableMap.putAll(persistentMap);
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
