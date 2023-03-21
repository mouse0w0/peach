package com.github.mouse0w0.peach.plugin;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PluginManagerImpl implements PluginManager {
    @Override
    public List<? extends Plugin> getPlugins() {
        return PluginManagerCore.getPlugins();
    }

    @Override
    public List<? extends Plugin> getEnabledPlugins() {
        return PluginManagerCore.getEnabledPlugins();
    }

    @Nullable
    @Override
    public Plugin getPlugin(String id) {
        return PluginManagerCore.getPlugin(id);
    }
}
