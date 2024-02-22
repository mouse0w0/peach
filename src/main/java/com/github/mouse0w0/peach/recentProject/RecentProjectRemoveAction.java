package com.github.mouse0w0.peach.recentProject;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.data.DataKeys;

public final class RecentProjectRemoveAction extends Action {
    @Override
    public void perform(ActionEvent event) {
        RecentProjectInfo info = (RecentProjectInfo) DataKeys.SELECTED_ITEM.get(event);
        RecentProjectsManager.getInstance().removeRecentProject(info.getPath());
    }
}
