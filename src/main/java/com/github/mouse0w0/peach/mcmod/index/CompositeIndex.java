package com.github.mouse0w0.peach.mcmod.index;

import org.apache.commons.collections4.collection.CompositeCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@SuppressWarnings("SuspiciousMethodCalls")
final class CompositeIndex<K, V> implements Index<K, V> {
    private final List<Map<K, V>> all = new ArrayList<>();

    public void addComposited(Map<K, V> map) {
        if (map != null) {
            all.add(map);
        }
    }

    public void removeComposited(Map<K, V> map) {
        all.remove(map);
    }

    @Override
    public int size() {
        int size = 0;
        for (Map<K, V> map : all) {
            size += map.size();
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        for (Map<K, V> map : all) {
            if (!map.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean containsKey(Object key) {
        for (Map<K, V> map : all) {
            if (map.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Map<K, V> map : all) {
            if (map.containsValue(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        for (Map<K, V> map : all) {
            if (map.containsKey(key)) {
                return map.get(key);
            }
        }
        return null;
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        for (Map<K, V> map : all) {
            if (map.containsKey(key)) {
                return map.get(key);
            }
        }
        return defaultValue;
    }

    @Override
    public Collection<K> keys() {
        CompositeCollection<K> compositeCollection = new CompositeCollection<>();
        for (Map<K, V> map : all) {
            compositeCollection.addComposited(map.keySet());
        }
        return compositeCollection;
    }

    @Override
    public Collection<V> values() {
        CompositeCollection<V> compositeCollection = new CompositeCollection<>();
        for (Map<K, V> map : all) {
            compositeCollection.addComposited(map.values());
        }
        return compositeCollection;
    }

    @Override
    public Collection<Map.Entry<K, V>> entries() {
        CompositeCollection<Map.Entry<K, V>> compositeCollection = new CompositeCollection<>();
        for (Map<K, V> map : all) {
            compositeCollection.addComposited(map.entrySet());
        }
        return compositeCollection;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        for (Map<K, V> map : all) {
            map.forEach(action);
        }
    }
}
