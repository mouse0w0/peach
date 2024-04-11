package com.github.mouse0w0.peach.mcmod.index;

import com.github.mouse0w0.peach.util.Validate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class GenericIndexProvider implements IndexProvider {
    private final Map<IndexKey<?, ?>, Map<?, ?>> indexes = new HashMap<>();

    private final String name;
    private final int order;

    public GenericIndexProvider(String name, int order) {
        this.name = Validate.notEmpty(name);
        this.order = order;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <K, V> Map<K, V> getIndex(IndexKey<K, V> indexKey) {
        return (Map<K, V>) indexes.computeIfAbsent(indexKey, $ -> new LinkedHashMap<>());
    }
}
