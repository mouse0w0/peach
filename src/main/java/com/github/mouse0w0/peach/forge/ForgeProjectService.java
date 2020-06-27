package com.github.mouse0w0.peach.forge;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.event.project.ProjectEvent;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.forge.ModInfoUI;
import com.github.mouse0w0.peach.ui.project.ProjectWindow;
import com.github.mouse0w0.peach.ui.project.WindowManager;
import com.github.mouse0w0.peach.util.JsonFile;

import java.nio.file.Files;
import java.nio.file.Path;

public class ForgeProjectService {

    private static final ForgeProjectService INSTANCE = new ForgeProjectService();

    public static ForgeProjectService getInstance() {
        return INSTANCE;
    }

    public ForgeProjectService() {
        Peach.getEventBus().addListener(this::onOpenedProject);
    }

    private void onOpenedProject(ProjectEvent.Opened event) {
        Project project = event.getProject();
        Path file = project.getPath().resolve(ForgeProjectInfo.FILE_NAME);
        JsonFile<ForgeProjectInfo> jsonFile = new JsonFile<>(file, ForgeProjectInfo.class, ForgeProjectInfo::new);
        jsonFile.load();
        project.putData(ForgeProjectInfo.KEY, jsonFile);
        if (!Files.exists(file)) {
            ProjectWindow window = WindowManager.getInstance().getProjectWindow(project);
            ModInfoUI.show(jsonFile, window.getStage());
        }
    }
}
