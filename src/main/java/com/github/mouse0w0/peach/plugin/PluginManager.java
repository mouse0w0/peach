package com.github.mouse0w0.peach.plugin;

import javax.annotation.Nullable;
import java.util.List;

public interface PluginManager {
    List<? extends Plugin> getPlugins();

    List<? extends Plugin> getEnabledPlugins();

    @Nullable
    Plugin getPlugin(String id);
}
