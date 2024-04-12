package com.github.mouse0w0.peach.mcmod.index;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GenericIndexProvider implements IndexProvider {
    private final Map<IndexKey<?, ?>, IndexEntries<?, ?>> indexes = new HashMap<>();

    @Override
    public final Set<IndexKey<?, ?>> getKeys() {
        return indexes.keySet();
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <K, V> IndexEntries<K, V> getEntries(IndexKey<K, V> key) {
        return (IndexEntries<K, V>) indexes.computeIfAbsent(key, k -> new IndexEntries<>());
    }
}
