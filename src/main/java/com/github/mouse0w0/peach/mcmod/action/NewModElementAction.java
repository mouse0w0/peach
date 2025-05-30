package com.github.mouse0w0.peach.mcmod.action;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.mcmod.ui.dialog.NewElementDialog;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.FileUtils;

import java.nio.file.Path;

public class NewModElementAction extends Action {
    @Override
    public void update(ActionEvent event) {
        Project project = DataKeys.PROJECT.get(event);
        Path path = DataKeys.PATH.get(event);
        Path sourcesPath = ResourceUtils.getResourcePath(project, ResourceUtils.ELEMENTS);
        if (path == null) {
            event.getPresentation().setVisible(true);
        } else {
            event.getPresentation().setVisible(path.startsWith(sourcesPath));
        }
    }

    @Override
    public void perform(ActionEvent event) {
        Project project = DataKeys.PROJECT.get(event);
        Path path = DataKeys.PATH.get(event);
        Path sourcesPath = ResourceUtils.getResourcePath(project, ResourceUtils.ELEMENTS);
        if (path == null) {
            NewElementDialog.show(project, sourcesPath);
        } else if (path.startsWith(sourcesPath)) {
            NewElementDialog.show(project, FileUtils.getDirectory(path));
        }
    }
}
