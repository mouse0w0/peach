package com.github.mouse0w0.peach.l10n;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.PropertyKey;

import java.util.ResourceBundle;

@ApiStatus.Internal
public final class AppL10n {
    private static final String BUNDLE = "l10n.zh-CN";
    private static final L10n INSTANCE = L10n.getL10n("peach");

    public static String localize(@PropertyKey(resourceBundle = BUNDLE) String key) {
        return INSTANCE.localize(key);
    }

    public static String localize(@PropertyKey(resourceBundle = BUNDLE) String key, Object... args) {
        return INSTANCE.localize(key, args);
    }

    @Nullable
    public static String localizeOrNull(@PropertyKey(resourceBundle = BUNDLE) String key) {
        return INSTANCE.localizeOrNull(key);
    }

    @Nullable
    public static String localizeOrNull(@PropertyKey(resourceBundle = BUNDLE) String key, Object... args) {
        return INSTANCE.localizeOrNull(key, args);
    }

    @Nullable
    public static String localizeOrDefault(@PropertyKey(resourceBundle = BUNDLE) String key, @Nullable String defaultValue) {
        return INSTANCE.localizeOrDefault(key, defaultValue);
    }

    @Nullable
    public static String localizeOrDefault(@PropertyKey(resourceBundle = BUNDLE) String key, @Nullable String defaultValue, Object... args) {
        return INSTANCE.localizeOrDefault(key, defaultValue, args);
    }

    public static ResourceBundle getResourceBundle() {
        return INSTANCE.getResourceBundle();
    }

    private AppL10n() {
    }
}
