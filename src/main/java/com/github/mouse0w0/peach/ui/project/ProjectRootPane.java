package com.github.mouse0w0.peach.ui.project;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionGroup;
import com.github.mouse0w0.peach.action.ActionManager;
import com.github.mouse0w0.peach.action.IdeGroups;
import com.github.mouse0w0.peach.project.Project;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

class ProjectRootPane extends BorderPane {

    private final Project project;

    private MenuBar menuBar;

    private TabPane tabPane;

    public ProjectRootPane(Project project) {
        this.project = project;
        setPrefSize(820, 600);
        setTop(menuBar = createMenuBar());
        setCenter(tabPane = new TabPane());

        getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
    }

    public TabPane getTabPane() {
        return tabPane;
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
