package com.github.mouse0w0.peach.l10n;

import com.github.mouse0w0.peach.util.SoftValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class L10nManagerImpl implements L10nManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("Localization");

    private final Set<Locale> availableLocales = new HashSet<>();
    private final Map<String, Map<Locale, List<L10nEP>>> plugin2Locale2Resources = new HashMap<>();
    private final Map<String, L10n> l10nCache = new SoftValueMap<>(new ConcurrentHashMap<>());
    private final Locale locale = Locale.getDefault();
    private final Locale fallbackLocale = Locale.US;

    public L10nManagerImpl() {
        for (L10nEP l10nEP : L10nEP.EXTENSION_POINT.getExtensions()) {
            Locale locale = Locale.forLanguageTag(l10nEP.getLocale());
            availableLocales.add(locale);
            plugin2Locale2Resources
                    .computeIfAbsent(l10nEP.getPluginId(), k -> new HashMap<>(1))
                    .computeIfAbsent(locale, k -> new ArrayList<>(1))
                    .add(l10nEP);
        }
    }

    @Override
    public Set<Locale> getAvailableLocales() {
        return availableLocales;
    }

    @Override
    public L10n getL10n(String pluginId) {
        return l10nCache.computeIfAbsent(pluginId, this::createL10n);
    }

    private L10n createL10n(String pluginId) {
        Map<Locale, List<L10nEP>> locale2Resources = plugin2Locale2Resources.get(pluginId);
        List<L10nEP> resources = locale2Resources.get(locale);
        if (resources == null) {
            resources = locale2Resources.get(fallbackLocale);
        }
        Map<String, String> lookup = new HashMap<>();
        for (L10nEP resource : resources) {
            String path = resource.getPath();
            InputStream is = resource.getPlugin().getClassLoader().getResourceAsStream(path);
            if (is == null) {
                LOGGER.error("Not found localization resource, provider=" + resource.getPlugin().getId() + ", path=" + path);
                continue;
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                loadProperties(br, lookup);
            } catch (IOException e) {
                LOGGER.error("Cannot load localization resource, provider=" + resource.getPlugin().getId() + ", path=" + path, e);
            }
        }
        return new L10nImpl(lookup);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void loadProperties(Reader reader, Map<String, String> result) throws IOException {
        Properties properties = new Properties();
        properties.load(reader);
        result.putAll((Map) properties);
    }
}
