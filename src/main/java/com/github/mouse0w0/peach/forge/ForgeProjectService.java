package com.github.mouse0w0.peach.forge;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.event.project.ProjectWindowEvent;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.forge.ModInfoUI;
import com.github.mouse0w0.peach.util.JsonFile;

import java.nio.file.Files;
import java.nio.file.Path;

public class ForgeProjectService {

    public static ForgeProjectService getInstance() {
        return Peach.getInstance().getService(ForgeProjectService.class);
    }

    public ForgeProjectService() {
        Peach.getEventBus().addListener(this::onOpenedProject);
    }

    private void onOpenedProject(ProjectWindowEvent.Opened event) {
        Project project = event.getProject();
        Path file = project.getPath().resolve(ForgeProjectInfo.FILE_NAME);
        JsonFile<ForgeProjectInfo> jsonFile = new JsonFile<>(file, ForgeProjectInfo.class, ForgeProjectInfo::new);
        jsonFile.load();
        project.putData(ForgeProjectInfo.KEY, jsonFile);
        if (!Files.exists(file)) {
            ModInfoUI.show(jsonFile, event.getWindow().getStage());
        }
    }
}
