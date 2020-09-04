package com.github.mouse0w0.peach.mcmod.service;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.event.project.ProjectEvent;
import com.github.mouse0w0.peach.event.project.ProjectWindowEvent;
import com.github.mouse0w0.peach.mcmod.content.ContentManager;
import com.github.mouse0w0.peach.mcmod.contentPack.ContentPack;
import com.github.mouse0w0.peach.mcmod.contentPack.ContentPackManager;
import com.github.mouse0w0.peach.mcmod.element.ElementManager;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.mcmod.project.McModDataKeys;
import com.github.mouse0w0.peach.mcmod.project.McModSettings;
import com.github.mouse0w0.peach.mcmod.ui.ElementViewUI;
import com.github.mouse0w0.peach.mcmod.ui.McModSettingsUI;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.project.ProjectWindow;
import com.github.mouse0w0.peach.util.JsonFile;
import javafx.scene.control.Tab;

import java.nio.file.Path;

public class McModService {

    public static McModService getInstance() {
        return Peach.getInstance().getService(McModService.class);
    }

    private final ContentPackManager contentPackManager = new ContentPackManager();
    private final ElementManager elementManager = new ElementManager();

    public McModService() {
        Peach.getEventBus().addListener(this::onOpenedProject);
        Peach.getEventBus().addListener(this::onOpenedProjectWindow);
    }

    public ContentPackManager getContentPackManager() {
        return contentPackManager;
    }

    public ElementManager getElementManager() {
        return elementManager;
    }

    private void onOpenedProject(ProjectEvent.Opened event) {
        Project project = event.getProject();
        Path file = project.getPath().resolve(McModSettings.FILE_NAME);
        JsonFile<McModSettings> jsonFile = new JsonFile<>(file, McModSettings.class, McModSettings::new);
        jsonFile.load();
        project.putData(McModDataKeys.MOD_SETTINGS, jsonFile);

        project.putData(McModDataKeys.SOURCES_PATH, project.getPath().resolve("sources"));
        project.putData(McModDataKeys.RESOURCES_PATH, project.getPath().resolve("resources"));

        ContentManager contentManager = new ContentManager();
        for (ContentPack contentPack : getContentPackManager().getContentPacks()) {
            contentManager.addContentPack(contentPack);
        }
        project.registerService(ContentManager.class, contentManager);

        ModelManager modelManager = new ModelManager();
        project.registerService(ModelManager.class, modelManager);
    }

    private void onOpenedProjectWindow(ProjectWindowEvent.Opened event) {
        ProjectWindow window = event.getWindow();
        Project project = window.getProject();
        JsonFile<McModSettings> modInfoFile = project.getData(McModDataKeys.MOD_SETTINGS);

        if (!modInfoFile.exists()) {
            McModSettingsUI.show(modInfoFile, event.getWindow().getStage());
        }

        Tab elementView = new Tab();
        elementView.setClosable(false);
        elementView.setText(I18n.translate("ui.element_view.title"));
        elementView.setContent(new ElementViewUI(project));
        window.openTab(elementView);
    }
}
