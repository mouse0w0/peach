package com.github.mouse0w0.peach.mcmod.index;

import com.google.common.base.CaseFormat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class IndexType<K, V> {
    private static final Map<String, IndexType<?, ?>> types = new ConcurrentHashMap<>();

    private final String name;
    private final String lowerCamelName;

    @SuppressWarnings("unchecked")
    public static <K, V> IndexType<K, V> of(String name) {
        return (IndexType<K, V>) types.computeIfAbsent(name, IndexType::new);
    }

    private IndexType(String name) {
        this.name = name;
        this.lowerCamelName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name);
    }

    public String getName() {
        return name;
    }

    public String getLowerCamelName() {
        return lowerCamelName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexType<?, ?> indexType = (IndexType<?, ?>) o;
        return name.equals(indexType.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "IndexType{" +
                "name='" + name + "'" +
                '}';
    }
}
