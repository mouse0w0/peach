package com.github.mouse0w0.peach.mcmod.util;

import com.github.mouse0w0.peach.mcmod.project.McModDescriptor;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.FileUtils;

import java.nio.file.Path;

public class TextureUtils {

    private static final Path MISSING_TEXTURE = FileUtils.toPath(TextureUtils.class.getResource("/texture/missing.png"));

    public static Path getMissingTexture() {
        return MISSING_TEXTURE;
    }

    public static Path getTexturePath(Project project) {
        return McModDescriptor.getInstance(project).getResourcesPath().resolve("textures");
    }

    public static Path getTextureFile(Project project, String textureName) {
        return McModDescriptor.getInstance(project).getResourcesPath().resolve("textures/" + textureName + ".png");
    }
}
