package com.github.mouse0w0.peach.recentProject;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.data.DataKey;

public abstract class RecentProjectBaseAction extends Action {
    public static final DataKey<RecentProjectInfo> RECENT_PROJECT_SELECTED_ITEM = DataKey.create("RECENT_PROJECT_SELECTED_ITEM");

    public static RecentProjectInfo getSelectedItem(ActionEvent event) {
        return RECENT_PROJECT_SELECTED_ITEM.get(event);
    }
}
