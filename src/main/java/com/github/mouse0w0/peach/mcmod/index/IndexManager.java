package com.github.mouse0w0.peach.mcmod.index;

import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.Validate;
import org.apache.commons.collections4.map.CompositeMap;

import java.util.*;

public final class IndexManager {
    private final List<IndexProvider> providers = new ArrayList<>();
    private final Map<IndexType<?, ?>, Map<?, ?>> indexes = new HashMap<>();

    public static IndexManager getInstance(Project project) {
        return project.getService(IndexManager.class);
    }

    public List<IndexProvider> getProviders() {
        return Collections.unmodifiableList(providers);
    }

    public void addProvider(IndexProvider provider) {
        Validate.notNull(provider);
        providers.add(provider);
        providers.sort(Comparator.naturalOrder());
    }

    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> getIndex(IndexType<K, V> indexType) {
        return (Map<K, V>) indexes.computeIfAbsent(indexType, this::composeIndex);
    }

    private <K, V> Map<K, V> composeIndex(IndexType<K, V> indexType) {
        CompositeMap<K, V> compositeMap = new CompositeMap<>();
        for (IndexProvider provider : providers) {
            compositeMap.addComposited(provider.getIndex(indexType));
        }
        return compositeMap;
    }
}
