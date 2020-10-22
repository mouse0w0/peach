package com.github.mouse0w0.peach.extension;

import org.apache.commons.lang3.Validate;

import java.util.List;

public final class ExtensionPoint<T> {

    private final String name;

    private ExtensionContainer<T> container;

    public static <T> ExtensionPoint<T> of(String name) {
        return new ExtensionPoint<>(name);
    }

    private ExtensionPoint(String name) {
        this.name = Validate.notNull(name);
    }

    public ExtensionContainer<T> getContainer() {
        if (container == null) {
            container = Extensions.getExtensionContainer(name);
        }
        return container;
    }

    public List<T> getExtensions() {
        return getContainer().getExtensions();
    }
}
