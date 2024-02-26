package com.github.mouse0w0.peach.welcome;

import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.recentProject.RecentProjectsManager;

public final class RecentProjectRemoveAction extends RecentProjectBaseAction {
    @Override
    public void perform(ActionEvent event) {
        RecentProjectsManager.getInstance().removeRecentProject(getSelectedItem(event).getPath());
    }
}
