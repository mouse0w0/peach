package com.github.mouse0w0.peach.mcmod.index;

import com.github.mouse0w0.peach.util.Validate;

import java.util.HashMap;
import java.util.Map;

public class IndexProvider implements Comparable<IndexProvider> {

    private final Map<Index<?>, Object> indexes = new HashMap<>();

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
    public final <T> T getIndex(Index<T> index) {
        return (T) indexes.computeIfAbsent(index, Index::create);
    }

    @Override
    public int compareTo(IndexProvider o) {
        return Integer.compare(priority, o.priority);
    }
}
