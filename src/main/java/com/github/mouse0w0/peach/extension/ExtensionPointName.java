package com.github.mouse0w0.peach.extension;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public final class ExtensionPointName<T> {
    private final String name;

    private ExtensionPoint<T> point;

    public static <T> ExtensionPointName<T> of(String name) {
        return new ExtensionPointName<>(name);
    }

    private ExtensionPointName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    @NotNull
    public ExtensionPoint<T> getPoint() {
        if (point == null) {
            point = Extensions.getPoint(name);
        }
        return point;
    }

    @NotNull
    public List<T> getExtensions() {
        return getPoint().getExtensions();
    }

    @Override
    public String toString() {
        return name;
    }
}
