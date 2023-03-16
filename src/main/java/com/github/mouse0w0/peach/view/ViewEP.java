package com.github.mouse0w0.peach.view;

import com.github.mouse0w0.peach.extension.Attribute;
import com.github.mouse0w0.peach.extension.ExtensionPointName;
import com.github.mouse0w0.peach.extension.PluginAware;
import com.github.mouse0w0.peach.javafx.geometry.EightPos;
import com.github.mouse0w0.peach.plugin.Plugin;
import com.github.mouse0w0.peach.plugin.PluginException;

public final class ViewEP implements PluginAware {
    public static final ExtensionPointName<ViewEP> EXTENSION_POINT = ExtensionPointName.of("peach.view");

    @Attribute("id")
    public String id;
    @Attribute("icon")
    public String icon;
    @Attribute("position")
    public EightPos position;
    @Attribute("factory")
    public String factoryName;

    private Plugin plugin;
    private volatile ViewFactory factory;

    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    public ViewFactory getFactory() {
        ViewFactory factory = this.factory;
        if (factory != null) return factory;

        synchronized (this) {
            factory = this.factory;
            if (factory != null) return factory;

            try {
                factory = (ViewFactory) plugin.getClassLoader().loadClass(factoryName).getConstructor().newInstance();
            } catch (Throwable e) {
                throw new PluginException("Cannot create ViewFactory"
                        + ", id=" + id
                        + ", factoryName=" + factoryName
                        + ", plugin=" + plugin.getId(), e);
            }
            this.factory = factory;
        }
        return factory;
    }
}
