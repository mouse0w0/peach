package com.github.mouse0w0.peach.l10n;

import com.github.mouse0w0.peach.Peach;

import java.util.Locale;
import java.util.Set;

public interface L10nManager {
    static L10nManager getInstance() {
        return Peach.getInstance().getService(L10nManager.class);
    }

    Set<Locale> getAvailableLocales();

    L10n get(String pluginId);
}
