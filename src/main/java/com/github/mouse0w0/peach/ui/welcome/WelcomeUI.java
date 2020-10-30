package com.github.mouse0w0.peach.ui.welcome;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.action.ActionManager;
import com.github.mouse0w0.peach.event.project.ProjectEvent;
import com.github.mouse0w0.peach.event.project.ProjectWindowEvent;
import com.github.mouse0w0.peach.project.ProjectManager;
import com.github.mouse0w0.peach.service.RecentProjectInfo;
import com.github.mouse0w0.peach.service.RecentProjectsManager;
import com.github.mouse0w0.peach.ui.Icons;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
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
        stage.setTitle(I18n.translate("ui.welcome.title"));
        stage.getIcons().setAll(Icons.PEACH_16X);
        stage.setResizable(false);
        stage.setOnHidden(event -> {
            if (ProjectManager.getInstance().getOpenedProjects().isEmpty()) {
                Peach.getInstance().exit();
            }
        });
        stage.show();
    }

    private static void onOpenedProject(ProjectWindowEvent.Opened event) {
        stage.hide();
    }

    private static void onClosedProject(ProjectEvent.Closed event) {
        if (ProjectManager.getInstance().getOpenedProjects().isEmpty()) {
            if (showWelcomeIfNoProjectOpened) show();
            else Peach.getInstance().exit();
        }
    }

    public WelcomeUI() {
        FXUtils.addStyleSheet(this, "style/welcome.css");

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

        Button newProject = new Button(
                I18n.translate("ui.welcome.new_project"),
                new ImageView(new Image("/icon/plus-thick.png")));
        newProject.setOnAction(this::doNewProject);
        Button openProject = new Button(
                I18n.translate("ui.welcome.open_project"),
                new ImageView(new Image("/icon/folder-open-outline.png")));
        openProject.setOnAction(this::doOpenProject);
        Button donate = new Button(
                I18n.translate("ui.welcome.donate"),
                new ImageView(new Image("/icon/gift-outline.png")));
        donate.setOnAction(this::doDonate);

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

    @FXML
    public void doNewProject(ActionEvent event) {
        ActionManager.getInstance().perform("NewProject", event);
    }

    @FXML
    public void doOpenProject(ActionEvent event) {
        ActionManager.getInstance().perform("OpenProject", event);
    }

    @FXML
    public void doDonate(ActionEvent event) {
        ActionManager.getInstance().perform("Donate", event);
    }
}
