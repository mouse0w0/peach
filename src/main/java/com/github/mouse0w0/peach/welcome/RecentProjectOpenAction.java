package com.github.mouse0w0.peach.welcome;

import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.project.ProjectManager;

import java.nio.file.Files;
import java.nio.file.Paths;

public final class RecentProjectOpenAction extends RecentProjectBaseAction {

    @Override
    public void update(ActionEvent event) {
        event.getPresentation().setDisable(Files.notExists(Paths.get(getSelectedItem(event).getPath())));
    }

    @Override
    public void perform(ActionEvent event) {
        ProjectManager.getInstance().openProject(Paths.get(getSelectedItem(event).getPath()));
    }
}
