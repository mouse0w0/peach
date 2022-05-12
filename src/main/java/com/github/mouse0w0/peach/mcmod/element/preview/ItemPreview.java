package com.github.mouse0w0.peach.mcmod.element.preview;

import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.mcmod.element.PreviewGenerator;
import com.github.mouse0w0.peach.mcmod.element.impl.MEItem;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.FileUtils;

import java.nio.file.Files;
import java.nio.file.Path;

public class ItemPreview implements PreviewGenerator<MEItem> {
    @Override
    public void generate(Project project, MEItem element, Path outputFile) {
        Identifier model = element.getModelPrototype();
        switch (model.getName()) {
            case "generated":
            case "handheld":
                String layer0 = element.getTextures().get("layer0");
                Path source = ResourceUtils.getTextureFile(project, layer0);
                if (Files.notExists(source)) {
                    FileUtils.forceCopySilently(ResourceUtils.getMissingTexture(), outputFile);
                } else {
                    FileUtils.forceCopySilently(source, outputFile);
                }
                break;
            default:
                break;
        }
    }
}
