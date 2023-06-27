package com.github.mouse0w0.peach.l10n;

import org.jetbrains.annotations.Nullable;

import java.util.ResourceBundle;

public interface L10n {
    static L10n getL10n(String pluginId) {
        return L10nManager.getInstance().getL10n(pluginId);
    }

    String localize(String key);

    String localize(String key, Object... args);

    @Nullable String localizeOrNull(String key);

    @Nullable String localizeOrNull(String key, Object... args);

    @Nullable String localizeOrDefault(String key, @Nullable String defaultValue);

    @Nullable String localizeOrDefault(String key, @Nullable String defaultValue, Object... args);

    ResourceBundle getResourceBundle();
}
