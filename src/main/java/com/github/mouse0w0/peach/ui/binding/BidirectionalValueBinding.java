package com.github.mouse0w0.peach.ui.binding;

import javafx.beans.WeakListener;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

import java.lang.ref.WeakReference;

public class BidirectionalValueBinding {

    public static <K, V> void bind(Property<V> property, ObservableMap<K, V> map, K key) {
        if (property == null) throw new NullPointerException("Property cannot be null");
        if (map == null) throw new NullPointerException("Map cannot be null");
        if (key == null) throw new NullPointerException("Key cannot be null");
        final MapBinding<K, V> binding = new MapBinding<>(property, map, key);
        property.setValue(map.get(key));
        property.addListener(binding);
        map.addListener(binding);
    }

    public static void unbind(Object obj1, Object obj2) {
        if (obj1 instanceof Property && obj2 instanceof ObservableMap) {
            final Property property = (Property) obj1;
            final ObservableMap map = (ObservableMap) obj2;
            final MapBinding binding = new MapBinding(property, map, null);
            property.removeListener(binding);
            map.removeListener(binding);
        }
    }

    public static class MapBinding<K, V> implements WeakListener, ChangeListener<V>, MapChangeListener<K, V> {
        private final WeakReference<Property<V>> property;
        private final WeakReference<ObservableMap<K, V>> map;
        private final K key;
        private final int cachedHashCode;
        private boolean updating;

        public MapBinding(Property<V> property, ObservableMap<K, V> map, K key) {
            this.cachedHashCode = property.hashCode() * map.hashCode();
            this.property = new WeakReference<>(property);
            this.map = new WeakReference<>(map);
            this.key = key;
        }

        public Property<V> getProperty() {
            return property.get();
        }

        public ObservableMap<K, V> getMap() {
            return map.get();
        }

        @Override
        public void changed(ObservableValue<? extends V> observable, V oldValue, V newValue) {
            if (!updating) {
                final Property<V> property = getProperty();
                final ObservableMap<K, V> map = getMap();
                if (property == null || map == null) {
                    if (property != null) {
                        property.removeListener(this);
                    }
                    if (map != null) {
                        map.removeListener(this);
                    }
                } else {
                    try {
                        updating = true;
                        map.put(key, newValue);
                    } finally {
                        updating = false;
                    }
                }
            }
        }

        @Override
        public void onChanged(Change<? extends K, ? extends V> change) {
            if (!updating) {
                final Property<V> property = getProperty();
                final ObservableMap<K, V> map = getMap();
                if (property == null || map == null) {
                    if (property != null) {
                        property.removeListener(this);
                    }
                    if (map != null) {
                        map.removeListener(this);
                    }
                } else {
                    try {
                        updating = true;
                        if (change.getKey().equals(key)) {
                            if (change.wasRemoved()) {
                                property.setValue(null);
                            } else {
                                property.setValue(change.getValueAdded());
                            }
                        }
                    } finally {
                        updating = false;
                    }
                }
            }
        }

        @Override
        public boolean wasGarbageCollected() {
            return property.get() == null || map.get() == null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final Object property = getProperty();
            final Object map = getMap();
            if (property == null || map == null) {
                return false;
            }

            final MapBinding<?, ?> that = (MapBinding<?, ?>) o;
            final Object thatProperty = that.getProperty();
            final Object thatMap = that.getMap();
            if (thatProperty == null || thatMap == null) {
                return false;
            }

            return property == thatProperty && map == thatMap;
        }

        @Override
        public int hashCode() {
            return cachedHashCode;
        }
    }
}
