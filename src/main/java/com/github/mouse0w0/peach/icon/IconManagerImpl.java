package com.github.mouse0w0.peach.icon;

import com.github.mouse0w0.peach.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public final class IconManagerImpl implements IconManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("Icon");

    private final Map<String, URL> iconUrls = new HashMap<>();
    private final Map<String, Icon> icons = new HashMap<>();

    public IconManagerImpl() {
        for (IconDefinitionEP iconDefinition : IconDefinitionEP.EXTENSION_POINT.getExtensions()) {
            String path = iconDefinition.getPath();
            Plugin plugin = iconDefinition.getPlugin();
            ClassLoader classLoader = plugin.getClassLoader();
            URL url;
            if (classLoader instanceof URLClassLoader ucl) {
                url = ucl.findResource(path);
            } else {
                url = classLoader.getResource(path);
            }
            if (url == null) {
                LOGGER.error("Not found icon definition, path={}, plugin={}", path, plugin.getId());
                continue;
            }
            try (InputStream stream = url.openStream()) {
                Properties properties = new Properties();
                properties.load(stream);
                properties.forEach((k, v) -> {
                    URL resource = classLoader.getResource((String) v);
                    if (resource != null) {
                        if (iconUrls.putIfAbsent((String) k, resource) != null) {
                            LOGGER.warn("Icon already exists, name={}, path={}, iconDefinition=(path={}, plugin={})", k, v, path, plugin.getName());
                        }
                    } else {
                        LOGGER.warn("Not found icon resource, name={}, path={}, iconDefinition=(path={}, plugin={})", k, v, path, plugin.getName());
                    }
                });
            } catch (IOException e) {
                LOGGER.error("Cannot load icon definition, path={}, plugin={}", path, plugin.getId(), e);
            }
        }
    }

    @Override
    public Icon getIcon(@NotNull String name) {
        Objects.requireNonNull(name);
        Icon icon = icons.get(name);
        if (icon == null) {
            icon = new IconImpl(name, iconUrls.get(name));
            icons.put(name, icon);
        }
        return icon;
    }
}
