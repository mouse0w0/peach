package com.github.mouse0w0.peach.extension;

import com.github.mouse0w0.peach.util.Validate;

import java.util.List;

public final class ExtensionPointName<T> {
    private final String name;

    private ExtensionPoint<T> point;

    public static <T> ExtensionPointName<T> of(String name) {
        return new ExtensionPointName<>(name);
    }

    private ExtensionPointName(String name) {
        this.name = Validate.notNull(name);
    }

    public ExtensionPoint<T> getPoint() {
        if (point == null) {
            point = Extensions.getPoint(name);
        }
        return point;
    }

    public List<T> getExtensions() {
        return getPoint().getExtensions();
    }
}
