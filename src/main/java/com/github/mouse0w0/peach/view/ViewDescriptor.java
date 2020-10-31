package com.github.mouse0w0.peach.view;

import com.github.mouse0w0.peach.extension.Attribute;
import com.github.mouse0w0.peach.extension.ExtensionPoint;
import com.github.mouse0w0.viewpane.geometry.EightPos;

public final class ViewDescriptor {

    public static final ExtensionPoint<ViewDescriptor> EXTENSION_POINT = ExtensionPoint.of("view");

    @Attribute("id")
    public String id;
    @Attribute("icon")
    public String icon;
    @Attribute("position")
    public EightPos position;
    @Attribute("factory")
    public ViewFactory factory;
}
