package com.github.mouse0w0.peach.icon;

import com.github.mouse0w0.peach.extension.Attribute;
import com.github.mouse0w0.peach.extension.ExtensionPointName;
import com.github.mouse0w0.peach.extension.PluginAware;
import com.github.mouse0w0.peach.extension.Required;
import com.github.mouse0w0.peach.plugin.Plugin;

public final class IconDefinitionEP implements PluginAware {
    public static final ExtensionPointName<IconDefinitionEP> EXTENSION_POINT = ExtensionPointName.of("peach.iconDefinition");

    @Attribute
    @Required
    private String path;
    private Plugin plugin;

    public String getPath() {
        return path;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }
}
