package com.github.mouse0w0.peach.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class WeakValueMap<K, V> implements Map<K, V> {
    private final Map<K, Reference<V>> map;
    private final ReferenceQueue<V> queue;

    public WeakValueMap(Map<K, Reference<V>> map) {
        this.map = map;
        this.queue = new ReferenceQueue<>();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V get(Object key) {
        return getReferenceValue(map.get(key));
    }

    @Override
    public V put(K key, V value) {
        processQueue();
        return getReferenceValue(map.put(key, new WeakValueReference<>(key, value, queue)));
    }

    @Override
    public V remove(Object key) {
        processQueue();
        Reference<V> ref = map.remove(key);
        if (ref == null) return null;
        V value = ref.get();
        ref.clear();
        return value;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    private void processQueue() {
        while (true) {
            WeakValueReference<K, V> ref = (WeakValueReference<K, V>) queue.poll();
            if (ref == null) {
                return;
            }
            map.remove(ref.getKey());
        }
    }

    private static <V> V getReferenceValue(Reference<V> ref) {
        return ref != null ? ref.get() : null;
    }

    private static final class WeakValueReference<K, V> extends WeakReference<V> {
        private final K key;

        public WeakValueReference(K key, V value, ReferenceQueue<? super V> q) {
            super(value, q);
            this.key = key;
        }

        public K getKey() {
            return key;
        }
    }
}
