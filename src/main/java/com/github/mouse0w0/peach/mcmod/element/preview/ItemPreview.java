package com.github.mouse0w0.peach.mcmod.element.preview;

import com.github.mouse0w0.peach.mcmod.element.PreviewGenerator;
import com.github.mouse0w0.peach.mcmod.element.impl.Item;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.FileUtils;

import java.nio.file.Files;
import java.nio.file.Path;

public class ItemPreview implements PreviewGenerator<Item> {
    @Override
    public void generate(Project project, Item element, Path outputFile) {
        String model = element.getModel();
        switch (model) {
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
