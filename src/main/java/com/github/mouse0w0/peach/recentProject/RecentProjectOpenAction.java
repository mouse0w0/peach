package com.github.mouse0w0.peach.recentProject;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.project.ProjectManager;

import java.nio.file.Files;
import java.nio.file.Paths;

public final class RecentProjectOpenAction extends Action {

    @Override
    public void update(ActionEvent event) {
        RecentProjectInfo info = (RecentProjectInfo) DataKeys.SELECTED_ITEM.get(event);
        setVisible(Files.exists(Paths.get(info.getPath())));
    }

    @Override
    public void perform(ActionEvent event) {
        RecentProjectInfo info = (RecentProjectInfo) DataKeys.SELECTED_ITEM.get(event);
        ProjectManager.getInstance().openProject(Paths.get(info.getPath()));
    }
}
