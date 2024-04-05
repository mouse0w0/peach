package com.github.mouse0w0.peach.mcmod.generator.util;

import com.github.mouse0w0.peach.mcmod.model.BlockstateTemplate;
import com.github.mouse0w0.peach.mcmod.model.ModelEntry;
import com.github.mouse0w0.peach.mcmod.model.ModelTemplate;
import com.github.mouse0w0.peach.util.ClassPathUtils;
import com.github.mouse0w0.peach.util.StringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static com.github.mouse0w0.peach.util.JsonUtils.*;

public class ModelUtils {
    public static final String MISSING = "missing";

    public static void generateModel(String namespace,
                                     String identifier,
                                     BlockstateTemplate blockstateTemplate,
                                     ModelTemplate modelTemplate,
                                     Map<String, String> textures,
                                     String particleTexture,
                                     Path output,
                                     Map<String, String> outputModels) throws IOException {
        for (Map.Entry<String, ModelEntry> entry : modelTemplate.getModels().entrySet()) {
            String modelKey = entry.getKey();
            ModelEntry modelEntry = entry.getValue();
            String modelPath = processModelPath(identifier, modelEntry.getName());
            Path source = ClassPathUtils.getPath("model/template/" + modelEntry.getTemplate(), modelTemplate.getPlugin().getClassLoader());
            Path target = output.resolve("models/" + modelPath + ".json");
            processModel(source, target, getActualParent(modelEntry.getParent(), namespace, outputModels), textures, particleTexture);
            outputModels.put(modelKey, modelPath);
        }
    }

    public static void generateCustomModel(String namespace,
                                           String identifier,
                                           BlockstateTemplate blockstateTemplate,
                                           Map<String, String> models,
                                           Path input,
                                           Map<String, String> textures,
                                           String particleTexture,
                                           Path output,
                                           Map<String, String> outputModels) throws IOException {
        for (Map.Entry<String, String> entry : blockstateTemplate.getModels().entrySet()) {
            String modelKey = entry.getKey();
            String modelPath = processModelPath(identifier, entry.getValue());
            Path source = input.resolve(models.get(modelKey) + ".json");
            Path target = output.resolve("models/" + modelPath + ".json");
            processModel(source, target, null, textures, particleTexture);
            outputModels.put(modelKey, modelPath);
        }
    }

    public static String getActualParent(String parent, String namespace, Map<String, String> models) {
        return StringUtils.isNotEmpty(parent) ? processResourcePath(namespace, models.get(parent)) : null;
    }

    public static Map<String, String> processTextures(String namespace, Map<String, String> textures) {
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, String> entry : textures.entrySet()) {
            result.put(entry.getKey(), processResourcePath(namespace, entry.getValue()));
        }
        return result;
    }

    public static String processResourcePath(String namespace, String path) {
        return namespace + ":" + path;
    }

    public static String processModelPath(String identifier, String path) {
        return path.replace("${id}", identifier);
    }

    public static void processModel(Path source, Path target, String parent, Map<String, String> textures, String particleTexture) throws IOException {
        writeJson(gsonPrettyPrinting(), target, processModel(readJson(source).getAsJsonObject(), parent, textures, particleTexture));
    }

    public static JsonObject processModel(JsonObject model, String parent, Map<String, String> textures, String particleTexture) {
        if (StringUtils.isNotEmpty(parent)) {
            model.addProperty("parent", parent);
        }
        JsonObject tex = model.getAsJsonObject("textures");
        if (tex != null) {
            for (Map.Entry<String, JsonElement> entry : tex.entrySet()) {
                String key = entry.getKey();
                String overrideKey = entry.getValue().getAsString();
                tex.addProperty(key, textures.getOrDefault(overrideKey.startsWith("$") ? overrideKey.substring(1) : key, MISSING));
            }
            if (StringUtils.isNotEmpty(particleTexture)) {
                tex.addProperty("particle", particleTexture);
            }
        }
        return model;
    }
}
