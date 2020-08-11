package com.github.mouse0w0.peach.mcmod.language;

import com.github.mouse0w0.peach.mcmod.element.ElementFile;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LanguageGenerator {
    public static Properties generate(String namespace, Collection<ElementFile<?>> elements) {
        Properties properties = new Properties();
        Map<String, String> localizedTexts = new HashMap<>();
        for (ElementFile<?> element : elements) {
            Object o = element.get();
            if (o instanceof Localizable) {
                localizedTexts.clear();
                ((Localizable) o).getLocalizedText(namespace, localizedTexts);
                properties.putAll(localizedTexts);
            }
        }
        return properties;
    }
}
