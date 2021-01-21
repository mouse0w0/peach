package com.github.mouse0w0.peach.icon;

import com.github.mouse0w0.peach.extension.Attribute;
import com.github.mouse0w0.peach.extension.ExtensionPoint;

public class IconSource {
    public static final ExtensionPoint<IconSource> EXTENSION_POINT = ExtensionPoint.of("iconSource");

    @Attribute("name")
    public String name;
    @Attribute("class")
    public Class<?> clazz;
}
