package com.github.mouse0w0.peach.javafx.util;

import com.github.mouse0w0.i18n.I18n;
import javafx.stage.FileChooser.ExtensionFilter;

public interface ExtensionFilters {
    ExtensionFilter JSON = create("JSON", "*.json");
    ExtensionFilter PNG = create("PNG", "*.png");
    ExtensionFilter OGG = create("OGG", "*.ogg");
    ExtensionFilter ZIP = create("ZIP", "*.zip");
    ExtensionFilter JAR = create("JAR", "*.jar");

    static ExtensionFilter create(final String description, final String... extensions) {
        return new ExtensionFilter(I18n.format("file.description", description), extensions);
    }
}
