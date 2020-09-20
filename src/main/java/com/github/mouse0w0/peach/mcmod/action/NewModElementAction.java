package com.github.mouse0w0.peach.mcmod.action;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.mcmod.dialog.NewElementDialog;
import com.github.mouse0w0.peach.ui.project.ProjectWindow;
import com.github.mouse0w0.peach.ui.project.WindowManager;

public class NewModElementAction extends Action {
    @Override
    public void perform(ActionEvent event) {
        ProjectWindow window = WindowManager.getInstance().getFocusedWindow();
        NewElementDialog.show(window.getProject(), window.getStage());
    }
}
