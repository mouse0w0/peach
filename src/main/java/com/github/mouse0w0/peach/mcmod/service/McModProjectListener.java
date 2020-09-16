package com.github.mouse0w0.peach.mcmod.service;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.event.project.ProjectEvent;
import com.github.mouse0w0.peach.event.project.ProjectWindowEvent;
import com.github.mouse0w0.peach.mcmod.dialog.ModSettingsDialog;
import com.github.mouse0w0.peach.mcmod.element.ElementManager;
import com.github.mouse0w0.peach.mcmod.project.McModDataKeys;
import com.github.mouse0w0.peach.mcmod.project.McModSettings;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.project.ProjectWindow;
import com.github.mouse0w0.peach.util.JsonFile;
import javafx.scene.control.Tab;

import java.nio.file.Path;

public class McModProjectListener {

    public static McModProjectListener getInstance() {
        return Peach.getInstance().getService(McModProjectListener.class);
    }

    public McModProjectListener() {
        Peach.getEventBus().addListener(this::onOpenedProject);
        Peach.getEventBus().addListener(this::onOpenedProjectWindow);
    }

    private void onOpenedProject(ProjectEvent.Opened event) {
        Project project = event.getProject();
        Path file = project.getPath().resolve(McModSettings.FILE_NAME);
        JsonFile<McModSettings> jsonFile = new JsonFile<>(file, McModSettings.class, McModSettings::new);
        jsonFile.load();
        project.putData(McModDataKeys.MOD_SETTINGS, jsonFile);

        project.putData(McModDataKeys.RESOURCES_PATH, project.getPath().resolve("resources"));
    }

    private void onOpenedProjectWindow(ProjectWindowEvent.Opened event) {
        ProjectWindow window = event.getWindow();
        Project project = window.getProject();
        JsonFile<McModSettings> modInfoFile = project.getData(McModDataKeys.MOD_SETTINGS);

        if (!modInfoFile.exists()) {
            ModSettingsDialog.show(modInfoFile, window.getStage());
        }

        Tab elementViewTab = new Tab();
        elementViewTab.setClosable(false);
        elementViewTab.setText(I18n.translate("ui.element_view.title"));
        elementViewTab.setContent(ElementManager.getInstance(project).getElementView());
        window.openTab(elementViewTab);
    }
}
