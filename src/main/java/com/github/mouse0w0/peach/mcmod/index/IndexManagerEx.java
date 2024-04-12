package com.github.mouse0w0.peach.mcmod.index;

import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
public interface IndexManagerEx extends IndexManager {
    <K, V> IndexEx<K, V> getIndexEx(IndexKey<K, V> key);

    void indexNonProjectEntries(List<IndexProvider> providers);
}
