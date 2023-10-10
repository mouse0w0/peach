package com.github.mouse0w0.peach.mcmod.action;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.fileEditor.FileEditorManager;
import com.github.mouse0w0.peach.mcmod.project.ModProjectService;
import com.github.mouse0w0.peach.project.Project;

public class OpenProjectSettingsAction extends Action {
    @Override
    public void perform(ActionEvent event) {
        Project project = DataKeys.PROJECT.get(event);
        if (project == null) return;
        FileEditorManager.getInstance(project).open(ModProjectService.getInstance(project).getMetadataFile());
    }
}
