package com.github.mouse0w0.peach.action.mainMenu;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.ui.newProject.NewProjectUI;
import com.github.mouse0w0.peach.ui.project.ProjectWindow;
import com.github.mouse0w0.peach.ui.project.WindowManager;

public class NewProjectAction extends Action {
    @Override
    public void perform(ActionEvent event) {
        ProjectWindow window = WindowManager.getInstance().getFocusedWindow();
        NewProjectUI.show(window != null ? window.getStage() : null);
    }
}
