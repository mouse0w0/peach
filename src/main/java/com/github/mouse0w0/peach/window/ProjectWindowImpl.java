package com.github.mouse0w0.peach.window;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionGroup;
import com.github.mouse0w0.peach.action.ActionGroups;
import com.github.mouse0w0.peach.action.ActionManager;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.data.DataManager;
import com.github.mouse0w0.peach.data.DataProvider;
import com.github.mouse0w0.peach.icon.AppIcon;
import com.github.mouse0w0.peach.icon.Icon;
import com.github.mouse0w0.peach.icon.IconManager;
import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.project.ProjectManager;
import com.github.mouse0w0.peach.ui.control.EightPos;
import com.github.mouse0w0.peach.ui.control.ViewPane;
import com.github.mouse0w0.peach.ui.control.ViewTab;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import com.github.mouse0w0.peach.view.ViewEP;
import com.github.mouse0w0.peach.window.status.StatusBar;
import com.github.mouse0w0.peach.window.status.StatusBarImpl;
import com.github.mouse0w0.peach.windowState.WindowStateService;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectWindowImpl implements ProjectWindow, DataProvider {
    private final Project project;
    private final Stage stage;
    private final ViewPane viewPane;
    private final TabPane tabPane;
    private final StatusBarImpl statusBar;

    ProjectWindowImpl(Project project) {
        this.project = project;

        viewPane = new ViewPane();
        VBox.setVgrow(viewPane, Priority.ALWAYS);
        initializeView(viewPane);

        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        viewPane.setCenter(tabPane);

        statusBar = new StatusBarImpl(project);

        VBox root = new VBox(createMenuBar(), viewPane, statusBar.getNode());
        root.setPrefSize(900, 600);

        Scene scene = new Scene(root);
        FXUtils.addStylesheet(scene, "style/style.css");

        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(project.getName());
        stage.getIcons().add(AppIcon.Peach.getImage());
        stage.setOnShown(this::onShown);
        stage.setOnHidden(this::onHidden);

        DataManager.getInstance().registerDataProvider(stage, this);
        WindowStateService.getInstance(project).setup(stage, "MainWindow");
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

    private MenuBar createMenuBar() {
        ActionManager actionManager = ActionManager.getInstance();
        ActionGroup mainMenuGroup = actionManager.getActionGroup(ActionGroups.MAIN_MENU);
        assert mainMenuGroup != null;
        MenuBar menuBar = new MenuBar();
        for (Action child : mainMenuGroup.getChildren(null)) {
            if (child instanceof ActionGroup group) {
                Menu menu = actionManager.createMenu(group);
                menuBar.getMenus().add(menu);
            }
        }
        return menuBar;
    }

    @Override
    public Project getProject() {
        return project;
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public ViewPane getViewPane() {
        return viewPane;
    }

    @Override
    public StatusBar getStatusBar() {
        return statusBar;
    }

    @Override
    public void show() {
        stage.show();
    }

    @Override
    public void hide() {
        stage.hide();
    }

    @Override
    public void requestFocus() {
        stage.requestFocus();
    }

    @Override
    public void openTab(Tab tab) {
        addTab(tab);
        selectTab(tab);
    }

    @Override
    public void addTab(Tab tab) {
        tabPane.getTabs().add(tab);
    }

    @Override
    public void selectTab(Tab tab) {
        tabPane.getSelectionModel().select(tab);
    }

    @Override
    public void removeTab(Tab tab) {
        tabPane.getTabs().remove(tab);
    }

    @Override
    public Object getData(@NotNull String key) {
        if (DataKeys.PROJECT.is(key)) {
            return project;
        }
        return null;
    }

    private void onShown(WindowEvent event) {
        project.getMessageBus().getPublisher(ProjectWindowListener.TOPIC).windowShown(this);
    }

    private void onHidden(WindowEvent event) {
        project.getMessageBus().getPublisher(ProjectWindowListener.TOPIC).windowHidden(this);
        ProjectManager.getInstance().closeProject(project);
    }
}
