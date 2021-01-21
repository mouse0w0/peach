package com.github.mouse0w0.peach.mcmod.index;

import com.github.mouse0w0.peach.project.Project;
import org.apache.commons.collections4.map.CompositeMap;
import org.apache.commons.lang3.Validate;

import java.util.*;

public final class IndexManager {

    private final List<IndexProvider> providers = new ArrayList<>();
    private final Map<Index<?, ?>, Map<?, ?>> indexes = new HashMap<>();

    public static IndexManager getInstance(Project project) {
        return project.getService(IndexManager.class);
    }

    public void registerProvider(IndexProvider provider) {
        Validate.notNull(provider);
        providers.add(provider);
        providers.sort(Comparator.naturalOrder());
    }

    public List<IndexProvider> getProviders() {
        return Collections.unmodifiableList(providers);
    }

    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> getIndex(Index<K, V> index) {
        return (Map<K, V>) indexes.computeIfAbsent(index, this::createIndex);
    }

    private <K, V> Map<K, V> createIndex(Index<K, V> index) {
        CompositeMap<K, V> map = new CompositeMap<>();
        for (IndexProvider provider : providers) {
            map.addComposited(provider.getIndex(index));
        }
        return map;
    }
}
