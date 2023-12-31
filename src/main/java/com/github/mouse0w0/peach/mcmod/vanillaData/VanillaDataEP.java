package com.github.mouse0w0.peach.mcmod.vanillaData;

import com.github.mouse0w0.peach.extension.Attribute;
import com.github.mouse0w0.peach.extension.ExtensionPointName;
import com.github.mouse0w0.peach.extension.PluginAware;
import com.github.mouse0w0.peach.extension.Required;
import com.github.mouse0w0.peach.plugin.Plugin;

public final class VanillaDataEP implements PluginAware {
    public static final ExtensionPointName<VanillaDataEP> EXTENSION_POINT = ExtensionPointName.of("peach.vanillaData");

    @Attribute
    @Required
    private String version;
    private Plugin plugin;

    public String getVersion() {
        return version;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }
}
