package com.github.mouse0w0.peach.wm;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionGroup;
import com.github.mouse0w0.peach.action.ActionGroups;
import com.github.mouse0w0.peach.action.ActionManager;
import com.github.mouse0w0.peach.javafx.FXUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.viewpane.ViewPane;
import javafx.event.Event;
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

        FXUtils.addStyleSheet(this, "style/style.css");

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
        MenuBar menuBar = new MenuBar();
        ActionManager actionManager = ActionManager.getInstance();
        ActionGroup group = (ActionGroup) actionManager.getAction(ActionGroups.MAIN_MENU);

        for (Action child : group.getChildren()) {
            if (!(child instanceof ActionGroup)) continue;

            Menu menu = actionManager.createMenu((ActionGroup) child);
            setOnShownEventHandler(menu);
            menuBar.getMenus().add(menu);
        }
        return menuBar;
    }

    private static final EventHandler<Event> ON_MENU_SHOWN = new EventHandler<Event>() {
        @Override
        public void handle(Event event) {
            Window window = FXUtils.getFocusedWindow().orElse(null);
            if (window != null) {
                ContextMenu contextMenu = (ContextMenu) window;
                Parent root = contextMenu.getScene().getRoot();
                for (Node node : root.lookupAll(".menu-item")) {
                    node.setOnMouseEntered(ON_MOUSE_ENTERED);
                    node.setOnMouseExited(ON_MOUSE_EXITED);
                }
            }
        }
    };

    private static final EventHandler<? super MouseEvent> ON_MOUSE_ENTERED = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Node node = (Node) event.getTarget();
            MenuItem menuItem = (MenuItem) node.getProperties().get(MenuItem.class);
            Action action = (Action) menuItem.getProperties().get(Action.class);
            StatusBarInfo statusBarInfo = StatusBarInfo.getInstance(WindowManager.getInstance().getFocusedProject());
            statusBarInfo.setText(action.getAppearance().getDescription());
        }
    };

    private static final EventHandler<? super MouseEvent> ON_MOUSE_EXITED = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            StatusBarInfo.getInstance(WindowManager.getInstance().getFocusedProject()).setText("");
        }
    };

    private static void setOnShownEventHandler(Menu menu) {
        menu.setOnShown(ON_MENU_SHOWN);
        for (MenuItem item : menu.getItems()) {
            if (item instanceof Menu) {
                setOnShownEventHandler((Menu) item);
            }
        }
    }
}
