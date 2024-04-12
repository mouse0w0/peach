package com.github.mouse0w0.peach.mcmod.index;

import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

public final class IndexEntries<K, V> {
    private final List<Object> entries = new ArrayList<>();

    public void add(K key, V value) {
        entries.add(key);
        entries.add(value);
    }

    @SuppressWarnings("unchecked")
    @ApiStatus.Internal
    public void collect(IndexEx<K, V> index) {
        for (int i = 0; i < entries.size(); i += 2) {
            index.addNonProjectEntry((K) entries.get(i), (V) entries.get(i + 1));
        }
    }
}
