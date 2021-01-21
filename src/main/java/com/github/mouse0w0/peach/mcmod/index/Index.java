package com.github.mouse0w0.peach.mcmod.index;

import java.util.Objects;

public final class Index<K, V> {
    private final String name;

    public static <K, V> Index<K, V> of(String name) {
        return new Index<>(name);
    }

    private Index(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Index<?, ?> index = (Index<?, ?>) o;
        return name.equals(index.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
