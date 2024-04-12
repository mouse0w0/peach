package com.github.mouse0w0.peach.mcmod.index;

import java.util.Set;

public interface IndexProvider {
    Set<IndexKey<?, ?>> getKeys();

    <K, V> IndexEntries<K, V> getEntries(IndexKey<K, V> indexKey);
}
