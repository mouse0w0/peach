package com.github.mouse0w0.peach.mcmod.action;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.mcmod.dialog.ModMetadataDialog;
import com.github.mouse0w0.peach.mcmod.project.McModDescriptor;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.project.WindowManager;

public class OpenProjectSettingsAction extends Action {
    @Override
    public void perform(ActionEvent event) {
        Project project = DataKeys.PROJECT.get(event);
        if (project == null) return;
        ModMetadataDialog.show(
                McModDescriptor.getInstance(project).getMetadata(),
                WindowManager.getInstance().getStage(project));
    }
}
