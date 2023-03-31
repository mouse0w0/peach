package com.github.mouse0w0.peach.l10n;

import com.github.mouse0w0.peach.extension.Attribute;
import com.github.mouse0w0.peach.extension.ExtensionPointName;
import com.github.mouse0w0.peach.extension.PluginAware;
import com.github.mouse0w0.peach.extension.Required;
import com.github.mouse0w0.peach.plugin.Plugin;

public final class L10nEP implements PluginAware {
    public static final ExtensionPointName<L10nEP> EXTENSION_POINT = ExtensionPointName.of("peach.localization");

    @Attribute("plugin")
    private String pluginId;
    @Attribute
    @Required
    private String locale;
    @Attribute
    @Required
    private String path;

    private Plugin plugin;

    public String getPluginId() {
        return pluginId;
    }

    public String getLocale() {
        return locale;
    }

    public String getPath() {
        return path;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
        if (this.pluginId == null) {
            this.pluginId = plugin.getId();
        }
    }
}
