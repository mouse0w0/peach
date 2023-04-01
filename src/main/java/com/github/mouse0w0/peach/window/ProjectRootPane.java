package com.github.mouse0w0.peach.window;

import com.github.mouse0w0.peach.action.*;
import com.github.mouse0w0.peach.javafx.FXUtils;
import com.github.mouse0w0.peach.javafx.control.ViewPane;
import com.github.mouse0w0.peach.project.Project;
import com.sun.javafx.scene.control.ContextMenuContent;
import com.sun.javafx.scene.control.MenuBarButton;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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
                    var windows = c.getList();
                    var from = c.getFrom();
                    var to = c.getTo();
                    for (int i = from; i < to; i++) {
                        Window window = windows.get(i);
                        if (window instanceof ContextMenu contextMenu) {
                            Node ownerNode = contextMenu.getOwnerNode();
                            if (ownerNode instanceof MenuBarButton button && button.menu instanceof ActionControl) {
                                applyStatusBarInfo(contextMenu);
                            } else if (ownerNode instanceof ContextMenuContent.MenuItemContainer container && container.getItem() instanceof ActionControl) {
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
            if (menuItem instanceof ActionControl actionControl && actionControl.getAction().getDescription() != null) {
                node.setOnMouseEntered(ON_MOUSE_ENTERED);
                node.setOnMouseExited(ON_MOUSE_EXITED);
            }
        }
    }

    private static final EventHandler<? super MouseEvent> ON_MOUSE_ENTERED = (EventHandler<MouseEvent>) event -> {
        Node node = (Node) event.getTarget();
        MenuItem menuItem = (MenuItem) node.getProperties().get(MenuItem.class);
        if (menuItem instanceof ActionControl actionControl) {
            Action action = actionControl.getAction();
            StatusBarInfo.getInstance(WindowManager.getInstance().getFocusedProject()).setText(action.getDescription());
        } else {
            StatusBarInfo.getInstance(WindowManager.getInstance().getFocusedProject()).setText(null);
        }
    };

    private static final EventHandler<? super MouseEvent> ON_MOUSE_EXITED = (EventHandler<MouseEvent>) event -> StatusBarInfo.getInstance(WindowManager.getInstance().getFocusedProject()).setText(null);
}
