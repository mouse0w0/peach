package com.github.mouse0w0.peach.recentProject;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.action.ActionMenuItem;
import com.github.mouse0w0.peach.data.DataKeys;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

public final class RecentProjectRemoveAction extends Action {
    @Override
    public void perform(ActionEvent event) {
        RecentProjectInfo info = (RecentProjectInfo) DataKeys.SELECTED_ITEM.get(event);
        RecentProjectManager.getInstance().removeRecentProject(info.getPath());
        ActionMenuItem source = (ActionMenuItem) event.getSource();
        ListCell<RecentProjectInfo> listCell = (ListCell<RecentProjectInfo>) source.getParentPopup().getProperties().get(Node.class);
        listCell.getListView().getItems().remove(info);
    }
}
