package com.github.mouse0w0.peach.forge;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.event.project.ProjectWindowEvent;
import com.github.mouse0w0.peach.forge.contentPack.ContentManager;
import com.github.mouse0w0.peach.forge.element.ElementManager;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.forge.ModInfoUI;
import com.github.mouse0w0.peach.util.JsonFile;

import java.nio.file.Files;
import java.nio.file.Path;

public class ForgeModService {

    public static ForgeModService getInstance() {
        return Peach.getInstance().getService(ForgeModService.class);
    }

    private final ContentManager contentManager = new ContentManager();
    private final ElementManager elementManager = new ElementManager();

    public ForgeModService() {
        Peach.getEventBus().addListener(this::onOpenedProject);
    }

    public ContentManager getContentManager() {
        return contentManager;
    }

    public ElementManager getElementManager() {
        return elementManager;
    }

    private void onOpenedProject(ProjectWindowEvent.Opened event) {
        Project project = event.getProject();
        Path file = project.getPath().resolve(ForgeModInfo.FILE_NAME);
        JsonFile<ForgeModInfo> jsonFile = new JsonFile<>(file, ForgeModInfo.class, ForgeModInfo::new);
        jsonFile.load();
        project.putData(ForgeModInfo.KEY, jsonFile);
        if (!Files.exists(file)) {
            ModInfoUI.show(jsonFile, event.getWindow().getStage());
        }
    }
}
