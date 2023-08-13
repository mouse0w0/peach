package com.github.mouse0w0.peach.action.file;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.newProject.NewProjectDialog;

public class NewProjectAction extends Action {
    @Override
    public void perform(ActionEvent event) {
        new NewProjectDialog().show();
    }
}
