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
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ModelManager {
    public static final Identifier CUSTOM = Identifier.of("buildin:custom");
    public static final Identifier INHERIT = Identifier.of("buildin:inherit");

    private final Map<String, BlockstateTemplate> blockstateTemplateMap = new HashMap<>();
    private final Map<Identifier, ModelTemplate> modelTemplateMap = new HashMap<>();
    private final Multimap<String, Identifier> blockstateToModelTemplates = HashMultimap.create();

    public static ModelManager getInstance() {
        return Peach.getInstance().getService(ModelManager.class);
    }

    public ModelManager() {
        load();
    }

    private void load() {
        try {
            Path state = ClassPathUtils.getPath("blockstate");
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(state)) {
                for (Path path : stream) {
                    if (FileUtils.getFileName(path).endsWith(".json")) {
                        loadBlockstateTemplate(path);
                    }
                }
            }

            Path model = ClassPathUtils.getPath("model");
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(model)) {
                for (Path path : stream) {
                    if (FileUtils.getFileName(path).endsWith(".json")) {
                        loadModelTemplate(path);
                    }
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void loadBlockstateTemplate(Path file) {
        try {
            BlockstateTemplate template = JsonUtils.readJson(file, BlockstateTemplate.class);
            String fileName = FileUtils.getFileName(file);
            String identifier = StringUtils.substringBefore(fileName, '.');
            blockstateTemplateMap.put(identifier, template);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void loadModelTemplate(Path file) {
        try {
            ModelTemplate template = JsonUtils.readJson(file, ModelTemplate.class);
            modelTemplateMap.put(template.getId(), template);
            if (template.getBlockstates() != null) {
                for (String group : template.getBlockstates()) {
                    blockstateToModelTemplates.put(group, template.getId());
                }
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to load file: " + file, e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public BlockstateTemplate getBlockstateTemplate(String blockstate) {
        return blockstateTemplateMap.get(blockstate);
    }

    public ModelTemplate getModelTemplate(Identifier identifier) {
        return modelTemplateMap.get(identifier);
    }

    public boolean hasModelTemplate(Identifier identifier) {
        return modelTemplateMap.containsKey(identifier);
    }

    public Collection<Identifier> getModelTemplatesByBlockstate(String blockstate) {
        return blockstateToModelTemplates.get(blockstate);
    }
}
