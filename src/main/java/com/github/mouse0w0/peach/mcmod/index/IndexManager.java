package com.github.mouse0w0.peach.mcmod.index;

import com.github.mouse0w0.peach.project.Project;
import org.apache.commons.lang3.Validate;

import java.util.*;

public final class IndexManager {

    private final List<IndexProvider> providers = new ArrayList<>();
    private final Map<Index<?>, Object> indexes = new HashMap<>();

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
    public <T> T getIndex(Index<T> index) {
        return (T) indexes.computeIfAbsent(index, this::compose);
    }

    private <T> T compose(Index<T> index) {
        return index.compose(providers.stream().map(provider -> provider.getIndex(index)));
    }
}
