package com.github.mouse0w0.peach.l10n;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Set;

final class EmptyL10n extends ResourceBundle implements L10n {
    public static final EmptyL10n INSTANCE = new EmptyL10n();

    private EmptyL10n() {
    }

    @Override
    public String localize(String key) {
        return key;
    }

    @Override
    public String localize(String key, Object... args) {
        return key;
    }

    @Override
    public @Nullable String localizeOrNull(String key) {
        return null;
    }

    @Override
    public @Nullable String localizeOrNull(String key, Object... args) {
        return null;
    }

    @Override
    public @Nullable String localizeOrDefault(String key, @Nullable String defaultValue) {
        return defaultValue;
    }

    @Override
    public @Nullable String localizeOrDefault(String key, @Nullable String defaultValue, Object... args) {
        return defaultValue != null ? defaultValue.formatted(args) : null;
    }

    @Override
    public ResourceBundle getResourceBundle() {
        return this;
    }

    @Override
    protected Object handleGetObject(@NotNull String key) {
        return null;
    }

    @NotNull
    @Override
    protected Set<String> handleKeySet() {
        return Collections.emptySet();
    }

    @NotNull
    @Override
    public Enumeration<String> getKeys() {
        return Collections.emptyEnumeration();
    }
}
