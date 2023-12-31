package com.github.mouse0w0.peach.mcmod.vanillaData;

import com.github.mouse0w0.peach.mcmod.index.IndexProvider;
import com.github.mouse0w0.peach.plugin.Plugin;

public interface VanillaData extends IndexProvider {
    String getVersion();

    Plugin getPlugin();
}
