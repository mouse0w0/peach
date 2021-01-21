package com.github.mouse0w0.peach.mcmod.index;

import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class IndexProvider implements Comparable<IndexProvider> {

    private final Map<Index<?, ?>, Map<?, ?>> indexes = new HashMap<>();

    private final String name;
    private final int priority;

    public IndexProvider(String name) {
        this(name, 0);
    }

    public IndexProvider(String name, int priority) {
        this.name = Validate.notEmpty(name);
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    @SuppressWarnings("unchecked")
    public final <K, V> Map<K, V> getIndex(Index<K, V> index) {
        return (Map<K, V>) indexes.computeIfAbsent(index, this::createIndex);
    }

    protected <K, V> Map<K, V> createIndex(Index<K, V> index) {
        return new LinkedHashMap<>();
    }

    @Override
    public int compareTo(IndexProvider o) {
        return Integer.compare(priority, o.priority);
    }
}
