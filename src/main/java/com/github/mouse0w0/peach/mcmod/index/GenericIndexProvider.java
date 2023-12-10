package com.github.mouse0w0.peach.mcmod.index;

import com.github.mouse0w0.peach.util.Validate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class GenericIndexProvider implements IndexProvider {
    private final Map<IndexType<?, ?>, Map<?, ?>> indexes = new HashMap<>();

    private final String name;
    private final int priority;

    public GenericIndexProvider(String name) {
        this(name, 0);
    }

    public GenericIndexProvider(String name, int priority) {
        this.name = Validate.notEmpty(name);
        this.priority = priority;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <K, V> Map<K, V> getIndex(IndexType<K, V> indexType) {
        return (Map<K, V>) indexes.computeIfAbsent(indexType, $ -> new LinkedHashMap<>());
    }
}
