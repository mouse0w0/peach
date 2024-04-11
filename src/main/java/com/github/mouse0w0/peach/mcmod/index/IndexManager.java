package com.github.mouse0w0.peach.mcmod.index;

import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.ListUtils;
import com.github.mouse0w0.peach.util.Validate;

import java.util.*;

public final class IndexManager {
    private static final Comparator<IndexProvider> INDEX_PROVIDER_COMPARATOR = Comparator.comparingInt(IndexProvider::getOrder);

    private final List<IndexProvider> providers = new ArrayList<>();
    private final Map<IndexKey<?, ?>, Index<?, ?>> indexes = new HashMap<>();

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
    public <K, V> Index<K, V> getIndex(IndexKey<K, V> indexKey) {
        return (Index<K, V>) indexes.computeIfAbsent(indexKey, this::createIndex);
    }

    private <K, V> Index<K, V> createIndex(IndexKey<K, V> indexKey) {
        CompositeIndex<K, V> index = new CompositeIndex<>();
        for (IndexProvider provider : providers) {
            index.addComposited(provider.getIndex(indexKey));
        }
        return index;
    }
}
