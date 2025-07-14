package com.github.mouse0w0.peach.welcome;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.action.ActionGroup;
import com.github.mouse0w0.peach.action.ActionManager;
import com.github.mouse0w0.peach.data.DataManager;
import com.github.mouse0w0.peach.icon.AppIcon;
import com.github.mouse0w0.peach.icon.IconManager;
import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.message.MessageBusConnection;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.project.ProjectLifecycleListener;
import com.github.mouse0w0.peach.project.ProjectManager;
import com.github.mouse0w0.peach.recentProject.RecentProject;
import com.github.mouse0w0.peach.recentProject.RecentProjectsChange;
import com.github.mouse0w0.peach.recentProject.RecentProjectsManager;
import com.github.mouse0w0.peach.style.StyleManager;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public final class WelcomeWindow extends Stage {
    private static final String NOT_EXISTS_CLASS = "not-exists";

    private final MessageBusConnection connection;

    private final ContextMenu contextMenu;
    private final ListView<RecentProject> projectListView;

    public static void showIfNoProjectOpened() {
        if (ProjectManager.getInstance().getOpenedProjects().isEmpty()) {
            showNow();
        }
    }

    public static void showNow() {
        new WelcomeWindow().show();
    }

    private WelcomeWindow() {
        setTitle(AppL10n.localize("welcome.title"));
        getIcons().add(AppIcon.Peach.getImage());
        setResizable(false);

        connection = Peach.getInstance().getMessageBus().connect();
        setOnHidden(event -> connection.disconnect());
        connection.subscribe(ProjectLifecycleListener.TOPIC, new ProjectLifecycleListener() {
            @Override
            public void projectOpened(Project project) {
                hide();
            }
        });

        ActionManager actionManager = ActionManager.getInstance();
        contextMenu = actionManager.createContextMenu((ActionGroup) actionManager.getAction("WelcomeWindow.RecentProjects"));

        projectListView = new ListView<>();
        projectListView.setId("project-list-view");
        projectListView.setPrefWidth(250);
        projectListView.setCellFactory(view -> new Cell());
        updateRecentProjects();
        DataManager.getInstance().registerDataProvider(projectListView, key -> {
            if (RecentProjectBaseAction.RECENT_PROJECT_SELECTED_ITEM.is(key))
                return projectListView.getSelectionModel().getSelectedItem();
            else return null;
        });

        connection.subscribe(RecentProjectsChange.TOPIC, this::updateRecentProjects);

        Button newProject = actionManager.createButton(actionManager.getAction("NewProject"));
        newProject.setText(AppL10n.localize("welcome.NewProject.text"));
        newProject.setGraphic(new ImageView(IconManager.getInstance().getIcon("Action.NewProject").getImage()));
        Button openProject = actionManager.createButton(actionManager.getAction("OpenProject"));
        Button donate = actionManager.createButton(actionManager.getAction("Donate"));

        VBox buttonList = new VBox(10, newProject, openProject, donate);
        buttonList.setAlignment(Pos.CENTER);
        StackPane.setAlignment(buttonList, Pos.CENTER);

        Label version = new Label(Peach.getInstance().getVersion().toString());
        version.setId("version");
        StackPane.setAlignment(version, Pos.BOTTOM_RIGHT);

        StackPane centerPane = new StackPane(buttonList, version);

        BorderPane root = new BorderPane();
        root.setId("welcome-window");
        root.setPrefSize(600, 400);
        root.setCenter(centerPane);
        root.setLeft(projectListView);

        Scene scene = new Scene(root);
        StyleManager.getInstance().apply(scene, "WelcomeWindow");
        setScene(scene);
    }

    private void updateRecentProjects() {
        ObservableList<RecentProject> items = projectListView.getItems();
        items.setAll(RecentProjectsManager.getInstance().getRecentProjects());
        items.sort(Comparator.comparingLong(RecentProject::getLatestOpenTimestamp).reversed());
    }

    private class Cell extends ListCell<RecentProject> {
        public Cell() {
            setOnMouseClicked(event -> {
                if (isEmpty()) return;
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    Path path = Paths.get(getItem().getPath());
                    if (Files.notExists(path)) return;
                    ProjectManager.getInstance().openProject(path);
                }
            });
        }

        @Override
        protected void updateItem(RecentProject item, boolean empty) {
            super.updateItem(item, empty);
            getStyleClass().remove(NOT_EXISTS_CLASS);
            if (empty) {
                setContextMenu(null);
                setText(null);
            } else {
                setContextMenu(contextMenu);
                setText(item.getName() + "\n" + item.getPath());
                if (Files.notExists(Paths.get(item.getPath()))) {
                    getStyleClass().add(NOT_EXISTS_CLASS);
                }
            }
        }
    }
}
