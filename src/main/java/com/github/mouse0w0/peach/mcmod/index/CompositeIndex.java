package com.github.mouse0w0.peach.mcmod.index;

import org.apache.commons.collections4.collection.CompositeCollection;
import org.apache.commons.collections4.set.CompositeSet;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

@SuppressWarnings({"unchecked", "SuspiciousMethodCalls"})
final class CompositeIndex<K, V> implements Index<K, V> {
    private Map<K, V>[] composite;

    public CompositeIndex() {
        this.composite = new Map[0];
    }

    public void addComposited(Map<K, V> map) {
        if (map != null) {
            Map<K, V>[] oldComposite = composite;
            Map<K, V>[] newComposite = new Map[oldComposite.length + 1];
            System.arraycopy(oldComposite, 0, newComposite, 0, oldComposite.length);
            newComposite[oldComposite.length] = map;
            composite = newComposite;
        }
    }

    @Override
    public int size() {
        int size = 0;
        for (Map<K, V> map : composite) {
            size += map.size();
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        for (Map<K, V> map : composite) {
            if (!map.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean containsKey(Object key) {
        for (Map<K, V> map : composite) {
            if (map.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Map<K, V> map : composite) {
            if (map.containsValue(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        for (Map<K, V> map : composite) {
            if (map.containsKey(key)) {
                return map.get(key);
            }
        }
        return null;
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        for (Map<K, V> map : composite) {
            if (map.containsKey(key)) {
                return map.get(key);
            }
        }
        return defaultValue;
    }

    @Override
    public Set<K> keySet() {
        CompositeSet<K> compositeSet = new CompositeSet<>();
        for (Map<K, V> map : composite) {
            compositeSet.addComposited(map.keySet());
        }
        return compositeSet;
    }

    @Override
    public Collection<V> values() {
        CompositeCollection<V> compositeCollection = new CompositeCollection<>();
        for (Map<K, V> map : composite) {
            compositeCollection.addComposited(map.values());
        }
        return compositeCollection;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        CompositeSet<Map.Entry<K, V>> compositeSet = new CompositeSet<>();
        for (Map<K, V> map : composite) {
            compositeSet.addComposited(map.entrySet());
        }
        return compositeSet;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        for (Map<K, V> map : composite) {
            map.forEach(action);
        }
    }
}
