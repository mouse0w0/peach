package com.github.mouse0w0.peach.ui.icon;

import com.github.mouse0w0.peach.Peach;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class IconManager {

    public static IconManager getInstance() {
        return Peach.getInstance().getService(IconManager.class);
    }

    private static Map<String, IconSource> providers = new HashMap<>();
    private static Map<String, Image> cacheIcons = new HashMap<>();

    public IconManager() {
        for (IconSource iconSource : IconSource.EXTENSION_POINT.getExtensions()) {
            if (providers.containsKey(iconSource.name)) {
                throw new IllegalStateException("Icon provider \"" + iconSource.name + "\" has been registered");
            }

            loadIcon(iconSource.clazz, iconSource.name + ".");
        }
    }

    private void loadIcon(Class<?> clazz, String prefix) {
        for (Field field : clazz.getFields()) {
            try {
                String iconName = prefix + field.getName();
                cacheIcons.put(iconName, (Image) field.get(null));
            } catch (IllegalAccessException ignored) {
            }
        }

        for (Class<?> inner : clazz.getClasses()) {
            loadIcon(inner, prefix + inner.getSimpleName() + ".");
        }
    }

    public Node createNode(String icon) {
        return new ImageView(cacheIcons.get(icon));
    }
}
