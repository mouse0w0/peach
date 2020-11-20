package com.github.mouse0w0.peach.action.file;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.project.ProjectManager;
import com.github.mouse0w0.peach.project.service.FileChooserHelper;

import java.io.File;

public class OpenProjectAction extends Action {
    @Override
    public void perform(ActionEvent event) {
        File file = FileChooserHelper.getInstance().openDirectory(null, "openProject", null);
        if (file == null) return;
        ProjectManager.getInstance().openProject(file.toPath());
    }
}
