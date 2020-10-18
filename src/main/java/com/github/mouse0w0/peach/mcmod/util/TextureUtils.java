package com.github.mouse0w0.peach.mcmod.util;

import com.github.mouse0w0.peach.mcmod.project.McModDataKeys;
import com.github.mouse0w0.peach.project.Project;

import java.nio.file.Path;

public class TextureUtils {

    public static Path getTexturePath(Project project) {
        return project.getData(McModDataKeys.RESOURCES_PATH).resolve("textures");
    }

    public static Path getTextureFile(Project project, String textureName) {
        return project.getData(McModDataKeys.RESOURCES_PATH).resolve("textures/" + textureName + ".png");
    }
}
