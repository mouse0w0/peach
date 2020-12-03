package com.github.mouse0w0.peach.ui.welcome;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.action.ActionManager;
import com.github.mouse0w0.peach.event.project.ProjectEvent;
import com.github.mouse0w0.peach.project.ProjectManager;
import com.github.mouse0w0.peach.service.RecentProjectInfo;
import com.github.mouse0w0.peach.service.RecentProjectsManager;
import com.github.mouse0w0.peach.ui.icon.Icons;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.nio.file.Paths;
import java.util.Comparator;

public class WelcomeUI extends BorderPane {

    private static final boolean showWelcomeIfNoProjectOpened = false;

    private static Stage stage;

    private ListView<RecentProjectInfo> recentProjects;

    static {
        Peach.getEventBus().addListener(WelcomeUI::onOpenedProject);
        Peach.getEventBus().addListener(WelcomeUI::onClosedProject);
    }

    public static void show() {
        stage = new Stage();
        stage.setScene(new Scene(new WelcomeUI()));
        stage.setTitle(I18n.translate("welcome.title"));
        stage.getIcons().setAll(Icons.Peach_16x);
        stage.setResizable(false);
        stage.setOnHidden(event -> {
            if (ProjectManager.getInstance().getOpenedProjects().isEmpty()) {
                Peach.getInstance().exit();
            }
        });
        stage.show();
    }

    private static void onOpenedProject(ProjectEvent.Opened event) {
        stage.hide();
    }

    private static void onClosedProject(ProjectEvent.Closed event) {
        if (ProjectManager.getInstance().getOpenedProjects().isEmpty()) {
            if (showWelcomeIfNoProjectOpened) show();
            else Peach.getInstance().exit();
        }
    }

    public WelcomeUI() {
        FXUtils.addStyleSheet(this, "style/style.css");

        setId("welcome");
        setPrefSize(600, 400);

        ContextMenu recentProjectsMenu = new ContextMenu();
        MenuItem open = new MenuItem(I18n.translate("common.open"));
        open.setOnAction(event -> ProjectManager.getInstance().openProject(
                Paths.get(recentProjects.getSelectionModel().getSelectedItem().getPath())));
        MenuItem remove = new MenuItem(I18n.translate("common.remove"));
        remove.setOnAction(event -> {
            RecentProjectInfo item = recentProjects.getSelectionModel().getSelectedItem();
            RecentProjectsManager.getInstance().removeRecentProject(item.getPath());
            recentProjects.getItems().remove(item);
        });
        recentProjectsMenu.getItems().addAll(open, remove);

        recentProjects = new ListView<>();
        recentProjects.setId("recent-projects");
        recentProjects.setPrefWidth(250);
        recentProjects.setCellFactory(list -> new ListCell<RecentProjectInfo>() {

            {
                setOnMouseClicked(event -> {
                    if (!isEmpty() && event.getClickCount() == 2) {
                        ProjectManager.getInstance().openProject(Paths.get(getItem().getPath()));
                    }
                });
            }

            @Override
            protected void updateItem(RecentProjectInfo item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setContextMenu(null);
                } else {
                    setText(item.getName() + "\n" + item.getPath());
                    setContextMenu(recentProjectsMenu);
                }
            }
        });
        recentProjects.getItems().addAll(RecentProjectsManager.getInstance().getRecentProjects());
        recentProjects.getItems().sort(Comparator.comparingLong(RecentProjectInfo::getLatestOpenTimestamp).reversed());
        recentProjects.getSelectionModel().selectFirst();
        setLeft(recentProjects);

        ActionManager actionManager = ActionManager.getInstance();

        Button newProject = actionManager.createButton(actionManager.getAction("NewProject"));
        newProject.setText(I18n.translate("welcome.NewProject.text"));
        newProject.setGraphic(new ImageView(Icons.Action.NewProject));
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
}
