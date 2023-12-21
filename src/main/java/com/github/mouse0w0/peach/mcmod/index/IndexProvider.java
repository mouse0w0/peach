package com.github.mouse0w0.peach.mcmod.index;

import java.util.Map;

public interface IndexProvider {
    String getName();

    int getPriority();

    <K, V> Map<K, V> getIndex(IndexType<K, V> indexType);
}
