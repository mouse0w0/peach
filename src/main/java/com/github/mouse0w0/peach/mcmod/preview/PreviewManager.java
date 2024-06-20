package com.github.mouse0w0.peach.mcmod.preview;

import com.github.mouse0w0.peach.mcmod.element.impl.BlockElement;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemElement;
import com.github.mouse0w0.peach.mcmod.project.ModProjectService;
import com.github.mouse0w0.peach.project.Project;
import javafx.scene.image.Image;

public interface PreviewManager {
    static PreviewManager getInstance(Project project) {
        return project.getService(ModProjectService.class).getPreviewManager();
    }

    Image getBlockImage(BlockElement element);

    Image getItemImage(ItemElement element);
}
