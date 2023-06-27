package com.github.mouse0w0.peach.welcome;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.action.ActionGroup;
import com.github.mouse0w0.peach.action.ActionManager;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.data.DataManager;
import com.github.mouse0w0.peach.icon.Icons;
import com.github.mouse0w0.peach.javafx.FXUtils;
import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.project.ProjectLifecycleListener;
import com.github.mouse0w0.peach.project.ProjectManager;
import com.github.mouse0w0.peach.recentProject.RecentProjectInfo;
import com.github.mouse0w0.peach.recentProject.RecentProjectManager;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class WelcomeUI extends BorderPane {

    private static final boolean showWelcomeIfNoProjectOpened = false;

    private static Stage stage;

    private ListView<RecentProjectInfo> recentProjects;

    private final ContextMenu contextMenu;
    private final EventHandler<Event> onContextMenuRequested;

    static {
        Peach.getInstance().getMessageBus().connect().subscribe(ProjectLifecycleListener.TOPIC, new ProjectLifecycleListener() {
            @Override
            public void projectOpened(Project project) {
                stage.hide();
            }

            @Override
            public void projectClosingBeforeSave(Project project) {

            }

            @Override
            public void projectClosing(Project project) {

            }

            @Override
            public void projectClosed(Project project) {
                if (ProjectManager.getInstance().getOpenedProjects().isEmpty()) {
                    if (showWelcomeIfNoProjectOpened) show();
                    else Peach.getInstance().exit();
                }
            }
        });
    }

    public static void show() {
        stage = new Stage();
        Scene scene = new Scene(new WelcomeUI());
        stage.setScene(scene);
        stage.setTitle(AppL10n.localize("welcome.title"));
        stage.getIcons().setAll(Icons.Peach.getImage());
        stage.setResizable(false);
        stage.setOnHidden(event -> {
            if (ProjectManager.getInstance().getOpenedProjects().isEmpty()) {
                Peach.getInstance().exit();
            }
        });
        stage.show();
    }

    public WelcomeUI() {
        FXUtils.addStylesheet(this, "style/style.css");

        setId("welcome");
        setPrefSize(600, 400);

        ActionManager actionManager = ActionManager.getInstance();
        ActionGroup filePopupMenu = (ActionGroup) actionManager.getAction("RecentProjectPopupMenu");
        contextMenu = actionManager.createContextMenu(filePopupMenu);
        onContextMenuRequested = event -> contextMenu.getProperties().put(Node.class, event.getSource());

        recentProjects = new ListView<>();
        recentProjects.setId("project-list");
        recentProjects.setPrefWidth(250);
        recentProjects.setCellFactory(list -> new Cell());
        recentProjects.getItems().addAll(RecentProjectManager.getInstance().getRecentProjects());
        recentProjects.getItems().sort(Comparator.comparingLong(RecentProjectInfo::getLatestOpenTimestamp).reversed());
        recentProjects.getSelectionModel().selectFirst();
        DataManager.getInstance().registerDataProvider(recentProjects, key -> {
            if (DataKeys.SELECTED_ITEM.is(key)) return recentProjects.getSelectionModel().getSelectedItem();
            else if (DataKeys.SELECTED_ITEMS.is(key)) return recentProjects.getSelectionModel().getSelectedItems();
            else return null;
        });
        setLeft(recentProjects);

        Button newProject = actionManager.createButton(actionManager.getAction("NewProject"));
        newProject.setText(AppL10n.localize("welcome.NewProject.text"));
        Button openProject = actionManager.createButton(actionManager.getAction("OpenProject"));
        Button donate = actionManager.createButton(actionManager.getAction("Donate"));

        VBox vBox = new VBox(10, newProject, openProject, donate);
        vBox.setAlignment(Pos.CENTER);
        StackPane.setAlignment(vBox, Pos.CENTER);

        Label version = new Label(Peach.getInstance().getVersion().toString());
        version.setId("version");
        StackPane.setAlignment(version, Pos.BOTTOM_RIGHT);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(vBox, version);

        setCenter(stackPane);
    }

    private class Cell extends ListCell<RecentProjectInfo> {

        public static final String PATH_NOT_EXISTS = "path-not-exists";

        public Cell() {
            setOnContextMenuRequested(onContextMenuRequested);
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
        protected void updateItem(RecentProjectInfo item, boolean empty) {
            super.updateItem(item, empty);
            getStyleClass().remove(PATH_NOT_EXISTS);
            if (empty) {
                setContextMenu(null);
                setText(null);
            } else {
                setContextMenu(contextMenu);
                setText(item.getName() + "\n" + item.getPath());
                if (Files.notExists(Paths.get(item.getPath()))) {
                    getStyleClass().add(PATH_NOT_EXISTS);
                }
            }
        }
    }
}
