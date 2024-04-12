package com.github.mouse0w0.peach.mcmod.index;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class IndexManagerImpl implements IndexManagerEx {
    private final Map<IndexKey<?, ?>, IndexEx<?, ?>> indexes = new HashMap<>();

    @Override
    public <K, V> Index<K, V> getIndex(IndexKey<K, V> key) {
        return getIndexEx(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <K, V> IndexEx<K, V> getIndexEx(IndexKey<K, V> key) {
        return (IndexEx<K, V>) indexes.computeIfAbsent(key, IndexImpl::new);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void indexNonProjectEntries(List<IndexProvider> providers) {
        for (IndexEx<?, ?> index : indexes.values()) {
            index.clearAllNonProjectEntries();
        }
        for (IndexProvider provider : providers) {
            for (IndexKey<?, ?> key : provider.getKeys()) {
                provider.getEntries(key).collect((IndexEx) getIndexEx(key));
            }
        }
    }
}
