package com.github.mouse0w0.peach.mcmod.index;

import java.util.Map;

public interface IndexProvider {
    String getName();

    int getOrder();

    <K, V> Map<K, V> getIndex(IndexKey<K, V> indexKey);
}
