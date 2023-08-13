package com.github.mouse0w0.peach.ui.util;

import com.github.mouse0w0.peach.l10n.AppL10n;
import javafx.stage.FileChooser.ExtensionFilter;

public interface ExtensionFilters {
    ExtensionFilter JSON = create("JSON", "*.json");
    ExtensionFilter PNG = create("PNG", "*.png");
    ExtensionFilter OGG = create("OGG", "*.ogg");
    ExtensionFilter ZIP = create("ZIP", "*.zip");
    ExtensionFilter JAR = create("JAR", "*.jar");

    static ExtensionFilter create(final String description, final String... extensions) {
        return new ExtensionFilter(AppL10n.localize("file.description", description), extensions);
    }
}
