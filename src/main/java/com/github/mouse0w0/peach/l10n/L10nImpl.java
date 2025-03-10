package com.github.mouse0w0.peach.l10n;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

final class L10nImpl extends ResourceBundle implements L10n {
    private final Map<String, String> lookup;

    public L10nImpl(Map<String, String> lookup) {
        this.lookup = lookup;
    }

    @Override
    public String localize(String key) {
        return localizeOrDefault(key, key);
    }

    @Override
    public String localize(String key, Object... args) {
        return localizeOrDefault(key, key, args);
    }

    @Override
    public @Nullable String localizeOrNull(String key) {
        return lookup.get(key);
    }

    @Override
    public @Nullable String localizeOrNull(String key, Object... args) {
        String s = lookup.get(key);
        return s != null ? s.formatted(args) : null;
    }

    @Override
    public @Nullable String localizeOrDefault(String key, @Nullable String defaultValue) {
        String result = lookup.get(key);
        return result != null ? result : defaultValue;
    }

    @Override
    public @Nullable String localizeOrDefault(String key, @Nullable String defaultValue, Object... args) {
        String s = lookup.get(key);
        if (s != null) {
            return s.formatted(args);
        }
        return defaultValue != null ? defaultValue.formatted(args) : null;
    }

    @Override
    public ResourceBundle getResourceBundle() {
        return this;
    }

    @Override
    protected Object handleGetObject(@NotNull String key) {
        return localize(key);
    }

    @NotNull
    @Override
    public Set<String> keySet() {
        return lookup.keySet();
    }

    @NotNull
    @Override
    protected Set<String> handleKeySet() {
        return lookup.keySet();
    }

    @NotNull
    @Override
    public Enumeration<String> getKeys() {
        return Collections.enumeration(lookup.keySet());
    }
}
