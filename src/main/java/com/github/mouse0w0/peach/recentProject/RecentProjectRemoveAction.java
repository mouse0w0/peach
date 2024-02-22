package com.github.mouse0w0.peach.recentProject;

import com.github.mouse0w0.peach.action.ActionEvent;

public final class RecentProjectRemoveAction extends RecentProjectBaseAction {
    @Override
    public void perform(ActionEvent event) {
        RecentProjectManager.getInstance().removeRecentProject(getSelectedItem(event).getPath());
    }
}
