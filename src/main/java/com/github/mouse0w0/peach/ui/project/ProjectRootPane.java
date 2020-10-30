package com.github.mouse0w0.peach.ui.project;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionGroup;
import com.github.mouse0w0.peach.action.ActionManager;
import com.github.mouse0w0.peach.action.IdeGroups;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;

class ProjectRootPane extends BorderPane {

    private final MenuBar menuBar;
    private final TabPane tabPane;
    private final StatusBarImpl statusBar;

    public ProjectRootPane(Project project) {
        FXUtils.addStyleSheet(this, "style/style.css");

        setPrefSize(820, 600);
        menuBar = createMenuBar();
        setTop(menuBar);
        tabPane = new TabPane();
        setCenter(tabPane);
        statusBar = new StatusBarImpl(project);
        setBottom(statusBar.getContent());

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public StatusBar getStatusBar() {
        return statusBar;
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        ActionManager actionManager = ActionManager.getInstance();
        ActionGroup group = (ActionGroup) actionManager.getAction(IdeGroups.MAIN_MENU);

        for (Action child : group.getChildren()) {
            if (!(child instanceof ActionGroup)) continue;

            Menu menu = actionManager.createMenu((ActionGroup) child);
            menuBar.getMenus().add(menu);
        }
        return menuBar;
    }
}
