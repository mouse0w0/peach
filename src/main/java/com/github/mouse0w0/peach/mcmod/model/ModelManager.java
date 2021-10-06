package com.github.mouse0w0.peach.mcmod.model;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.util.ClassPathUtils;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ModelManager {

    private final Map<Identifier, BlockStateTemplate> blockStateTemplateMap = new HashMap<>();
    private final Map<Identifier, ModelTemplate> modelTemplateMap = new HashMap<>();
    private final Multimap<String, Identifier> groupToModelTemplatesMap = HashMultimap.create();

    public static ModelManager getInstance() {
        return Peach.getInstance().getService(ModelManager.class);
    }

    public ModelManager() {
        load();
    }

    private void load() {
        try {
            Path blockstate = ClassPathUtils.getPath("blockstate");
            if (blockstate == null) throw new Error();
            Path model = ClassPathUtils.getPath("model");
            if (model == null) throw new Error();

            Files.list(blockstate)
                    .filter(path -> FileUtils.getFileName(path).endsWith(".meta.json"))
                    .forEach(this::loadBlockStateTemplate);
            Files.list(model)
                    .filter(path -> FileUtils.getFileName(path).endsWith(".model.json"))
                    .forEach(this::loadModelTemplate);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void loadModelTemplate(Path file) {
        try {
            ModelTemplate template = JsonUtils.readJson(file, ModelTemplate.class);
            modelTemplateMap.put(template.getIdentifier(), template);
            groupToModelTemplatesMap.put(template.getGroup(), template.getIdentifier());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void loadBlockStateTemplate(Path file) {
        try {
            BlockStateTemplate template = JsonUtils.readJson(file, BlockStateTemplate.class);
            blockStateTemplateMap.put(template.getIdentifier(), template);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public BlockStateTemplate getBlockStateTemplate(Identifier identifier) {
        return blockStateTemplateMap.get(identifier);
    }

    public ModelTemplate getModelTemplate(Identifier identifier) {
        return modelTemplateMap.get(identifier);
    }

    public Collection<Identifier> getModelTemplatesByGroup(String group) {
        return groupToModelTemplatesMap.get(group);
    }
}
