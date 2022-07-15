package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2;

import com.github.mouse0w0.peach.util.JsonUtils;
import org.apache.commons.lang3.reflect.TypeUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Type;
import java.util.Map;

public final class Srgs {
    private static final Type STRING_TO_STRING_MAP = TypeUtils.parameterize(Map.class, String.class, String.class);

    public static final Map<String, String> MATERIALS = load("material.srg.json");
    public static final Map<String, String> MAP_COLORS = load("map_color.srg.json");
    public static final Map<String, String> SOUND_TYPES = load("sound_type.srg.json");

    private static Map<String, String> load(String name) {
        try {
            return JsonUtils.readJson(Srgs.class.getResource(name), STRING_TO_STRING_MAP);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
