package com.github.mouse0w0.peach.action.mainMenu;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.project.ProjectManager;

public class CloseProjectAction extends Action {
    @Override
    public void perform(ActionEvent event) {
        Project project = DataKeys.PROJECT.get(event);
        if (project == null) return;
        ProjectManager.getInstance().closeProject(project);
    }
}
