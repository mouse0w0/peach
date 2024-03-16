package com.github.mouse0w0.peach.action.file;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.fileChooser.FileChooserHelper;
import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.project.ProjectManager;

import java.io.File;

public class OpenProjectAction extends Action {
    @Override
    public void perform(ActionEvent event) {
        File file = FileChooserHelper.getInstance().openDirectory(null, "openProject", AppL10n.localize("fileChooser.openProject.title"), null);
        if (file == null) return;
        ProjectManager.getInstance().openProject(file.toPath());
    }
}
