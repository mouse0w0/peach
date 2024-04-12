package com.github.mouse0w0.peach.mcmod.index;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface IndexEx<K, V> extends Index<K, V> {
    boolean addProjectEntry(K key, V value);

    boolean addNonProjectEntry(K key, V value);

    boolean removeProjectEntry(Object key);

    boolean removeNonProjectEntry(Object key);

    void clearAllProjectEntries();

    void clearAllNonProjectEntries();
}
