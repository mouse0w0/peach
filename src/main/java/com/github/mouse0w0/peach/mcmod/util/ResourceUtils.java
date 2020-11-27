package com.github.mouse0w0.peach.mcmod.util;

import com.github.mouse0w0.peach.project.Project;

import java.io.InputStream;
import java.nio.file.Path;

public class ResourceUtils {

    public static final String LANG = "lang";

    public static final String BLOCK_STATES = "blockstates";

    public static final String MODELS = "models";
    public static final String BLOCK_MODELS = MODELS + "/blocks";
    public static final String ITEM_MODELS = MODELS + "/items";

    public static final String TEXTURES = "textures";
    public static final String BLOCK_TEXTURES = TEXTURES + "/blocks";
    public static final String ITEM_TEXTURES = TEXTURES + "/items";
    public static final String GUI_TEXTURES = TEXTURES + "/gui";

    public static Path getResourcePath(Project project, String resource) {
        return project.getPath().resolve("resources/" + resource);
    }

    public static Path getTextureFile(Project project, String textureName) {
        return project.getPath().resolve("resources/" + ResourceUtils.TEXTURES + "/" + textureName + ".png");
    }

    public static InputStream getMissingTexture() {
        return ResourceUtils.class.getResourceAsStream("/image/mcmod/missing.png");
    }
}
