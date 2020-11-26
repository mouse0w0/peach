package com.github.mouse0w0.peach.mcmod.project;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.event.project.ProjectWindowEvent;
import com.github.mouse0w0.peach.mcmod.dialog.ModMetadataDialog;
import com.github.mouse0w0.peach.mcmod.element.ElementManager;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.project.ProjectWindow;
import com.github.mouse0w0.peach.util.JsonFile;
import javafx.scene.control.Tab;

public final class McModProjectListener {

    public McModProjectListener() {
        Peach.getEventBus().addListener(this::onOpenedProjectWindow);
    }

    private void onOpenedProjectWindow(ProjectWindowEvent.Opened event) {
        ProjectWindow window = event.getWindow();
        Project project = window.getProject();
        JsonFile<McModMetadata> modInfoFile = McModDescriptor.getInstance(project).getMetadata();

        if (!modInfoFile.exists()) {
            ModMetadataDialog.show(modInfoFile, window.getStage());
        }

        Tab elementViewTab = new Tab();
        elementViewTab.setClosable(false);
        elementViewTab.setText(I18n.translate("tab.elementView.title"));
        elementViewTab.setContent(ElementManager.getInstance(project).getElementView());
        window.openTab(elementViewTab);
    }
}
