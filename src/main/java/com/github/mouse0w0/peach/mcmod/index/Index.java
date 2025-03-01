package com.github.mouse0w0.peach.mcmod.index;

import javafx.collections.ObservableList;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;

public interface Index<K, V> {
    IndexKey<K, V> getKey();

    int size();

    boolean isEmpty();

    boolean containsKey(Object key);

    boolean containsValue(Object value);

    V get(Object key);

    V getOrDefault(Object key, V defaultValue);

    Collection<K> keys();

    ObservableList<K> keyList();

    Collection<V> values();

    Collection<Map.Entry<K, V>> entries();

    void forEach(BiConsumer<? super K, ? super V> action);
}
