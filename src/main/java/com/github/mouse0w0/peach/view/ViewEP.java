package com.github.mouse0w0.peach.view;

import com.github.mouse0w0.peach.extension.Attribute;
import com.github.mouse0w0.peach.extension.ExtensionPointName;
import com.github.mouse0w0.peach.extension.PluginAware;
import com.github.mouse0w0.peach.javafx.geometry.EightPos;
import com.github.mouse0w0.peach.plugin.Plugin;
import com.github.mouse0w0.peach.plugin.PluginException;
import com.github.mouse0w0.peach.util.LazyInitializer;

public final class ViewEP implements PluginAware {
    public static final ExtensionPointName<ViewEP> EXTENSION_POINT = ExtensionPointName.of("peach.view");

    private static final LazyInitializer<ViewEP, ViewFactory> FACTORY;

    static {
        FACTORY = new LazyInitializer<>(ViewEP.class, "factory", ViewFactory.class, viewEP -> {
            try {
                return (ViewFactory) viewEP.plugin.getClassLoader().loadClass(viewEP.factoryName).getConstructor().newInstance();
            } catch (Throwable e) {
                throw new PluginException("Cannot create ViewFactory"
                        + ", id=" + viewEP.id
                        + ", factory=" + viewEP.factoryName
                        + ", plugin=" + viewEP.plugin.getId(), e);
            }
        });
    }

    @Attribute
    private String id;
    @Attribute
    private String icon;
    @Attribute
    private EightPos position;
    @Attribute("factory")
    private String factoryName;
    private Plugin plugin;
    private volatile ViewFactory factory;

    public String getId() {
        return id;
    }

    public String getIcon() {
        return icon;
    }

    public EightPos getPosition() {
        return position;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    public ViewFactory getFactory() {
        return FACTORY.get(this);
    }
}
