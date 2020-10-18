package com.github.mouse0w0.peach.mcmod.element.preview;

import com.github.mouse0w0.peach.mcmod.element.PreviewGenerator;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemElement;
import com.github.mouse0w0.peach.mcmod.util.TextureUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.FileUtils;

import java.nio.file.Path;

public class ItemPreview implements PreviewGenerator<ItemElement> {
    @Override
    public void generate(Project project, ItemElement element, Path outputFile) {
        String model = element.getModel();
        switch (model) {
            case "generated":
            case "handheld":
                String layer0 = element.getTextures().get("layer0");
                Path source = TextureUtils.getTextureFile(project, layer0);
                FileUtils.forceCopySilently(source, outputFile);
                break;
            default:
                break;
        }
    }
}
