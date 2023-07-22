package com.github.mouse0w0.peach.window;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionGroup;
import com.github.mouse0w0.peach.action.ActionGroups;
import com.github.mouse0w0.peach.action.ActionManager;
import com.github.mouse0w0.peach.icon.Icon;
import com.github.mouse0w0.peach.icon.IconManager;
import com.github.mouse0w0.peach.javafx.control.ViewPane;
import com.github.mouse0w0.peach.javafx.control.ViewTab;
import com.github.mouse0w0.peach.javafx.geometry.EightPos;
import com.github.mouse0w0.peach.javafx.util.FXUtils;
import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.view.ViewEP;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        initializeView(viewPane);
        setCenter(viewPane);

        tabPane = new TabPane();
        tabPane.setStyle("-fx-open-tab-animation: none; -fx-close-tab-animation: none;");
        tabPane.setMinSize(100, 100);
        viewPane.setCenter(tabPane);

        statusBar = new StatusBarImpl(project);
        setBottom(statusBar.getContent());

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
    }

    private void initializeView(ViewPane viewPane) {
        Logger logger = LoggerFactory.getLogger("View");
        for (ViewEP view : ViewEP.EXTENSION_POINT.getExtensions()) {
            String id = view.getId();
            if (id == null || id.isEmpty()) {
                logger.error("The id of view is null or empty, skip initialize.");
                continue;
            }

            String text = AppL10n.localize("view." + id + ".text");
            Icon icon = IconManager.getInstance().getIcon(view.getIcon());
            Node graphic = icon != null ? new ImageView(icon.getImage()) : null;
            Node content = view.getFactory().createViewContent(project);

            EightPos position = view.getPosition();
            if (position == null) {
                logger.warn("The position of view `{}` is null, set to default `LEFT_TOP`.", id);
                position = EightPos.LEFT_TOP;
            }

            viewPane.getViewGroup(position).getTabs().add(new ViewTab(text, graphic, content));
        }
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
}
