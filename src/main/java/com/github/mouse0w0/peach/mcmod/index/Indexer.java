package com.github.mouse0w0.peach.mcmod.index;

public interface Indexer {
    <K, V> void add(IndexKey<K, V> indexKey, K key, V value);
}
