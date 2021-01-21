package com.github.mouse0w0.peach.mcmod.action;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.mcmod.dialog.NewElementDialog;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.wm.WindowManager;

public class NewModElementAction extends Action {
    @Override
    public void perform(ActionEvent event) {
        Project project = DataKeys.PROJECT.get(event);
        if (project == null) return;
        NewElementDialog.show(project, WindowManager.getInstance().getStage(project));
    }
}
