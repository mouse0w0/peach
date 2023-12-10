package com.github.mouse0w0.peach.mcmod.index;

import java.util.Map;

public interface IndexProvider extends Comparable<IndexProvider> {
    String getName();

    int getPriority();

    <K, V> Map<K, V> getIndex(IndexType<K, V> indexType);

    @Override
    default int compareTo(IndexProvider o) {
        return Integer.compare(getPriority(), o.getPriority());
    }
}
