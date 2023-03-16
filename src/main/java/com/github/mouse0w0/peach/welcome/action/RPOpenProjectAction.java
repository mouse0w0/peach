package com.github.mouse0w0.peach.welcome.action;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.application.service.RecentProjectInfo;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.project.ProjectManager;

import java.nio.file.Files;
import java.nio.file.Paths;

public final class RPOpenProjectAction extends Action {

    @Override
    public void update(ActionEvent event) {
        RecentProjectInfo info = (RecentProjectInfo) DataKeys.SELECTED_ITEM.get(event);
        getAppearance().setVisible(Files.exists(Paths.get(info.getPath())));
    }

    @Override
    public void perform(ActionEvent event) {
        RecentProjectInfo info = (RecentProjectInfo) DataKeys.SELECTED_ITEM.get(event);
        ProjectManager.getInstance().openProject(Paths.get(info.getPath()));
    }
}
