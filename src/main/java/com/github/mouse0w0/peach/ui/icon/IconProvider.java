package com.github.mouse0w0.peach.ui.icon;

import com.github.mouse0w0.peach.extension.Attribute;
import com.github.mouse0w0.peach.extension.ExtensionPoint;

public class IconProvider {
    public static final ExtensionPoint<IconProvider> EXTENSION_POINT = ExtensionPoint.of("iconProvider");

    @Attribute("name")
    public String name;
    @Attribute("class")
    public Class<?> clazz;
}
