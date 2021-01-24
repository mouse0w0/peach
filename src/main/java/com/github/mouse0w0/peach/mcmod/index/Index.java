package com.github.mouse0w0.peach.mcmod.index;

import java.util.stream.Stream;

public abstract class Index<T> {
    private final String name;

    public Index(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract T create();

    public abstract T compose(Stream<T> stream);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Index<?> index = (Index<?>) o;
        return name.equals(index.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
