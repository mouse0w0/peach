package com.github.mouse0w0.peach.mcmod.index;

import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.ListUtils;
import com.github.mouse0w0.peach.util.Validate;

import java.util.*;

public final class IndexManager {
    private static final Comparator<IndexProvider> INDEX_PROVIDER_COMPARATOR = (a, b) -> Integer.compare(b.getPriority(), a.getPriority());

    private final List<IndexProvider> providers = new ArrayList<>();
    private final Map<IndexType<?, ?>, Index<?, ?>> indexes = new HashMap<>();

    public static IndexManager getInstance(Project project) {
        return project.getService(IndexManager.class);
    }

    public List<IndexProvider> getProviders() {
        return Collections.unmodifiableList(providers);
    }

    public void addProvider(IndexProvider provider) {
        Validate.notNull(provider);
        ListUtils.binarySearchInsert(providers, provider, INDEX_PROVIDER_COMPARATOR);
    }

    @SuppressWarnings("unchecked")
    public <K, V> Index<K, V> getIndex(IndexType<K, V> indexType) {
        return (Index<K, V>) indexes.computeIfAbsent(indexType, this::createIndex);
    }

    private <K, V> Index<K, V> createIndex(IndexType<K, V> indexType) {
        CompositeIndex<K, V> index = new CompositeIndex<>();
        for (IndexProvider provider : providers) {
            index.addComposited(provider.getIndex(indexType));
        }
        return index;
    }
}
