package com.github.mouse0w0.peach.mcmod.model;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.util.ClassPathUtils;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.github.mouse0w0.peach.util.StringUtils;
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
    public static final Identifier CUSTOM = new Identifier("buildin:custom");
    public static final Identifier INHERIT = new Identifier("buildin:inherit");

    private final Map<String, Blockstate> blockstateMap = new HashMap<>();
    private final Map<Identifier, ModelPrototype> modelTemplateMap = new HashMap<>();
    private final Multimap<String, Identifier> groupToModelTemplatesMap = HashMultimap.create();

    public static ModelManager getInstance() {
        return Peach.getInstance().getService(ModelManager.class);
    }

    public ModelManager() {
        load();
    }

    private void load() {
        try {
            Path state = ClassPathUtils.getPath("blockstate");
            if (state == null) throw new Error();

            Files.list(state)
                    .filter(path -> FileUtils.getFileName(path).endsWith(".json"))
                    .forEach(this::loadStateTemplate);

            Path model = ClassPathUtils.getPath("model");
            if (model == null) throw new Error();

            Files.list(model)
                    .filter(path -> FileUtils.getFileName(path).endsWith(".json"))
                    .forEach(this::loadModelTemplate);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void loadStateTemplate(Path file) {
        try {
            Blockstate template = JsonUtils.readJson(file, Blockstate.class);
            String fileName = FileUtils.getFileName(file);
            String identifier = StringUtils.substringBefore(fileName, '.');
            blockstateMap.put(identifier, template);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void loadModelTemplate(Path file) {
        try {
            ModelPrototype template = JsonUtils.readJson(file, ModelPrototype.class);
            modelTemplateMap.put(template.getIdentifier(), template);
            if (template.getGroups() != null) {
                for (String group : template.getGroups()) {
                    groupToModelTemplatesMap.put(group, template.getIdentifier());
                }
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to load file: " + file, e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Blockstate getBlockstate(String blockstate) {
        return blockstateMap.get(blockstate);
    }

    public ModelPrototype getModelTemplate(Identifier identifier) {
        return modelTemplateMap.get(identifier);
    }

    public Collection<Identifier> getModelTemplatesByGroup(String group) {
        return groupToModelTemplatesMap.get(group);
    }
}
