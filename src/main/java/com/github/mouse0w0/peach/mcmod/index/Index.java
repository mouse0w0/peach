package com.github.mouse0w0.peach.mcmod.index;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public interface Index<K, V> {
    int size();

    boolean isEmpty();

    boolean containsKey(Object key);

    boolean containsValue(Object value);

    V get(Object key);

    V getOrDefault(Object key, V defaultValue);

    Set<K> keySet();

    Collection<V> values();

    Set<Map.Entry<K, V>> entrySet();

    void forEach(BiConsumer<? super K, ? super V> action);
}
