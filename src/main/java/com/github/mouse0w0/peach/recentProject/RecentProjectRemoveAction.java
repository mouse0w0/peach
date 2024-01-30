package com.github.mouse0w0.peach.recentProject;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.action.ActionMenuItem;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.data.DataManagerImpl;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.stage.PopupWindow;

public final class RecentProjectRemoveAction extends Action {
    @Override
    public void perform(ActionEvent event) {
        RecentProjectInfo info = (RecentProjectInfo) DataKeys.SELECTED_ITEM.get(event);
        RecentProjectManager.getInstance().removeRecentProject(info.getPath());
        ActionMenuItem source = (ActionMenuItem) event.getEvent().getSource();
        ListCell<RecentProjectInfo> listCell = (ListCell<RecentProjectInfo>) getOwnerNode(source.getParentPopup());
        listCell.getListView().getItems().remove(info);
    }

    private static Node getOwnerNode(PopupWindow popupWindow) {
        Node ownerNode = popupWindow.getOwnerNode();
        if (ownerNode != null) {
            return ownerNode;
        }
        return (Node) popupWindow.getProperties().get(DataManagerImpl.OWNER_NODE);
    }
}
