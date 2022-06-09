package com.github.mouse0w0.peach.mcmod.compiler.util;

import com.github.mouse0w0.peach.util.StringUtils;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static com.github.mouse0w0.peach.util.JsonUtils.*;

public class ModelUtils {
    public static Map<String, String> preprocessTextures(String namespace, Map<String, String> textures, String particleTexture) {
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, String> entry : textures.entrySet()) {
            result.put(entry.getKey(), namespace + ":" + entry.getValue());
        }
        if (StringUtils.isNotEmpty(particleTexture)) {
            result.put("particle", namespace + ":" + particleTexture);
        }
        return result;
    }

    public static void applyModel(Path source, Path target, Map<String, String> textures, boolean applyParticle) throws IOException {
        writeJson(gsonPrettyPrinting(), target, applyTextures(readJson(source).getAsJsonObject(), textures, applyParticle));
    }

    public static JsonObject applyTextures(JsonObject model, Map<String, String> textures, boolean applyParticle) {
        JsonObject tex = model.getAsJsonObject("textures");
        for (String key : tex.keySet()) {
            tex.addProperty(key, textures.get(key));
        }
        if (applyParticle) {
            tex.addProperty("particle", textures.get("particle"));
        }
        return model;
    }
}
