package com.github.mouse0w0.peach.mcmod.action;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.mcmod.dialog.NewElementDialog;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.window.WindowManager;

import java.nio.file.Path;

public class NewModElementAction extends Action {
    @Override
    public void update(ActionEvent event) {
        Project project = DataKeys.PROJECT.get(event);
        Path path = DataKeys.PATH.get(event);
        if (project == null || path == null) {
            getAppearance().setVisible(false);
            return;
        }

        Path sourcesPath = ResourceUtils.getResourcePath(project, ResourceUtils.SOURCES);
        getAppearance().setVisible(path.startsWith(sourcesPath));
    }

    @Override
    public void perform(ActionEvent event) {
        Project project = DataKeys.PROJECT.get(event);
        Path path = DataKeys.PATH.get(event);
        if (project == null || path == null) return;
        Path sourcesPath = ResourceUtils.getResourcePath(project, ResourceUtils.SOURCES);
        if (!path.startsWith(sourcesPath)) return;
        NewElementDialog.show(project, FileUtils.getDirectory(path), WindowManager.getInstance().getStage(project));
    }
}
