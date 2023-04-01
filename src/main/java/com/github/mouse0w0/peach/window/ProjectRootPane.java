package com.github.mouse0w0.peach.window;

import com.github.mouse0w0.peach.action.*;
import com.github.mouse0w0.peach.javafx.FXUtils;
import com.github.mouse0w0.peach.javafx.control.ViewPane;
import com.github.mouse0w0.peach.project.Project;
import com.sun.javafx.scene.control.ContextMenuContent;
import com.sun.javafx.scene.control.MenuBarButton;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;

class ProjectRootPane extends BorderPane {
    private final Project project;
    private final MenuBar menuBar;
    private final ViewPane viewPane;
    private final TabPane tabPane;
    private final StatusBarImpl statusBar;

    public ProjectRootPane(Project project) {
        this.project = project;

        FXUtils.addStylesheet(this, "style/style.css");

        setPrefSize(900, 600);

        menuBar = createMenuBar();
        setTop(menuBar);

        viewPane = new ViewPane();
        setCenter(viewPane);

        tabPane = new TabPane();
        tabPane.setStyle("-fx-open-tab-animation: none; -fx-close-tab-animation: none;");
        tabPane.setMinSize(100, 100);
        viewPane.setCenter(tabPane);

        statusBar = new StatusBarImpl(project);
        setBottom(statusBar.getContent());

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

        Window.getWindows().addListener((ListChangeListener<Window>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Window window : c.getAddedSubList()) {
                        if (window instanceof ContextMenu contextMenu) {
                            Node ownerNode = contextMenu.getOwnerNode();
                            if (window instanceof ActionHolder) {
                                applyStatusBarInfo(contextMenu);
                            } else if (ownerNode instanceof MenuBarButton button && button.menu instanceof ActionHolder) {
                                applyStatusBarInfo(contextMenu);
                            } else if (ownerNode instanceof ContextMenuContent.MenuItemContainer container && container.getItem() instanceof ActionHolder) {
                                applyStatusBarInfo(contextMenu);
                            }
                        }
                    }
                }
            }
        });
    }

    public Project getProject() {
        return project;
    }

    public ViewPane getViewPane() {
        return viewPane;
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public StatusBar getStatusBar() {
        return statusBar;
    }

    private MenuBar createMenuBar() {
        ActionManager actionManager = ActionManager.getInstance();
        ActionGroup mainMenuGroup = actionManager.getActionGroup(ActionGroups.MAIN_MENU);
        assert mainMenuGroup != null;
        MenuBar menuBar = new MenuBar();
        for (Action child : mainMenuGroup.getChildren()) {
            if (child instanceof ActionGroup group) {
                Menu menu = actionManager.createMenu(group);
                menuBar.getMenus().add(menu);
            }
        }
        return menuBar;
    }

    private static void applyStatusBarInfo(ContextMenu contextMenu) {
        Parent root = contextMenu.getScene().getRoot();
        for (Node node : root.lookupAll(".menu-item")) {
            MenuItem menuItem = FXUtils.getProperty(node, MenuItem.class);
            if (menuItem instanceof ActionHolder actionHolder && actionHolder.getAction().getDescription() != null) {
                node.hoverProperty().addListener(HOVER_LISTENER);
            }
        }
    }

    private static final InvalidationListener HOVER_LISTENER = observable -> {
        ReadOnlyBooleanProperty hoverProperty = (ReadOnlyBooleanProperty) observable;
        if (hoverProperty.get()) {
            Node node = (Node) hoverProperty.getBean();
            MenuItem menuItem = FXUtils.getProperty(node, MenuItem.class);
            assert menuItem != null;
            Action action = ((ActionHolder) menuItem).getAction();
            StatusBarInfo.getFocusedInstance().setText(action.getDescription());
        } else {
            StatusBarInfo.getFocusedInstance().setText(null);
        }
    };
}
