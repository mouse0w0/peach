package com.github.mouse0w0.peach.mcmod.model;

import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.mcmod.project.ModProjectService;
import com.github.mouse0w0.peach.mcmod.vanillaData.VanillaData;
import com.github.mouse0w0.peach.project.Project;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelManager {
    public static final Identifier CUSTOM = Identifier.of("buildin:custom");
    public static final Identifier INHERIT = Identifier.of("buildin:inherit");

    private final Map<String, BlockstateTemplate> blockstateTemplateMap = new HashMap<>();
    private final Map<Identifier, ModelTemplate> modelTemplateMap = new HashMap<>();
    private final Multimap<String, Identifier> blockstateToModelTemplates = HashMultimap.create();

    public static ModelManager getInstance(Project project) {
        return ModProjectService.getInstance(project).getModelManager();
    }

    public ModelManager(VanillaData vanillaData) {
        blockstateTemplateMap.putAll(vanillaData.getBlockstateTemplateMap());
        modelTemplateMap.putAll(vanillaData.getModelTemplateMap());
        for (ModelTemplate modelTemplate : vanillaData.getModelTemplateMap().values()) {
            List<String> blockstates = modelTemplate.getBlockstates();
            if (blockstates != null) {
                for (String blockstate : blockstates) {
                    blockstateToModelTemplates.put(blockstate, modelTemplate.getId());
                }
            }
        }
    }

    public BlockstateTemplate getBlockstateTemplate(String blockstate) {
        return blockstateTemplateMap.get(blockstate);
    }

    public ModelTemplate getModelTemplate(Identifier identifier) {
        return modelTemplateMap.get(identifier);
    }

    public Collection<Identifier> getModelTemplatesByBlockstate(String blockstate) {
        return blockstateToModelTemplates.get(blockstate);
    }
}
