package com.github.mouse0w0.peach.mcmod.vanillaData;

import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.mcmod.index.IndexProvider;
import com.github.mouse0w0.peach.mcmod.model.BlockstateTemplate;
import com.github.mouse0w0.peach.mcmod.model.ModelTemplate;
import com.github.mouse0w0.peach.plugin.Plugin;

import java.util.Map;

public interface VanillaData extends IndexProvider {
    String getVersion();

    Plugin getPlugin();

    Map<String, BlockstateTemplate> getBlockstateTemplateMap();

    Map<Identifier, ModelTemplate> getModelTemplateMap();
}
