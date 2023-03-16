package com.github.mouse0w0.peach.welcome.action;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.action.ActionMenuItem;
import com.github.mouse0w0.peach.application.service.RecentProjectInfo;
import com.github.mouse0w0.peach.application.service.RecentProjectsManager;
import com.github.mouse0w0.peach.data.DataKeys;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

public final class RPRemoveProjectAction extends Action {
    @Override
    public void perform(ActionEvent event) {
        RecentProjectInfo info = (RecentProjectInfo) DataKeys.SELECTED_ITEM.get(event);
        RecentProjectsManager.getInstance().removeRecentProject(info.getPath());
        ActionMenuItem source = (ActionMenuItem) event.getEvent().getSource();
        ListCell<RecentProjectInfo> listCell = (ListCell<RecentProjectInfo>) source.getParentPopup().getProperties().get(Node.class);
        listCell.getListView().getItems().remove(info);
    }
}
