package com.github.mouse0w0.peach.action.mainMenu;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.mcmod.compiler.Compiler;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.project.WindowManager;

public class BuildProjectAction extends Action {
    @Override
    public void perform(ActionEvent event) {
        Project project = WindowManager.getInstance().getFocusedWindow().getProject();
        new Compiler(project.getPath(), project.getPath().resolve("build")).run();
    }
}
