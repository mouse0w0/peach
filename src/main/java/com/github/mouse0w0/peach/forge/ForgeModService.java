package com.github.mouse0w0.peach.forge;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.event.project.ProjectEvent;
import com.github.mouse0w0.peach.event.project.ProjectWindowEvent;
import com.github.mouse0w0.peach.forge.contentPack.ContentManager;
import com.github.mouse0w0.peach.forge.element.ElementManager;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.forge.ElementViewUI;
import com.github.mouse0w0.peach.ui.forge.ModInfoUI;
import com.github.mouse0w0.peach.ui.project.ProjectWindow;
import com.github.mouse0w0.peach.util.JsonFile;
import javafx.scene.control.Tab;

import java.nio.file.Path;

public class ForgeModService {

    public static ForgeModService getInstance() {
        return Peach.getInstance().getService(ForgeModService.class);
    }

    private final ContentManager contentManager = new ContentManager();
    private final ElementManager elementManager = new ElementManager();

    public ForgeModService() {
        Peach.getEventBus().addListener(this::onOpenedProject);
        Peach.getEventBus().addListener(this::onOpenedProjectWindow);
    }

    public ContentManager getContentManager() {
        return contentManager;
    }

    public ElementManager getElementManager() {
        return elementManager;
    }

    private void onOpenedProject(ProjectEvent.Opened event) {
        Project project = event.getProject();
        Path file = project.getPath().resolve(ForgeModInfo.FILE_NAME);
        JsonFile<ForgeModInfo> jsonFile = new JsonFile<>(file, ForgeModInfo.class, ForgeModInfo::new);
        jsonFile.load();
        project.putData(ForgeProjectDataKeys.MOD_INFO_FILE, jsonFile);

        project.putData(ForgeProjectDataKeys.SOURCES_PATH, project.getPath().resolve("sources"));
        project.putData(ForgeProjectDataKeys.RESOURCES_PATH, project.getPath().resolve("resources"));
    }

    private void onOpenedProjectWindow(ProjectWindowEvent.Opened event) {
        ProjectWindow window = event.getWindow();
        Project project = window.getProject();
        JsonFile<ForgeModInfo> modInfoFile = project.getData(ForgeProjectDataKeys.MOD_INFO_FILE);

        if (!modInfoFile.exists()) {
            ModInfoUI.show(modInfoFile, event.getWindow().getStage());
        }

        Tab elementView = new Tab();
        elementView.setClosable(false);
        elementView.setText(I18n.translate("ui.element_view.title"));
        elementView.setContent(new ElementViewUI(project));
        window.openTab(elementView);
    }
}
