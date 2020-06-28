package com.github.mouse0w0.peach.ui;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.RecentProjectInfo;
import com.github.mouse0w0.peach.RecentProjectsManager;
import com.github.mouse0w0.peach.event.project.ProjectWindowEvent;
import com.github.mouse0w0.peach.project.ProjectManager;
import com.github.mouse0w0.peach.ui.project.NewProjectUI;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;

public class WelcomeUI extends BorderPane {

    private static final boolean showWelcomeIfNoProjectOpened = true;

    private static Stage stage;

    @FXML
    public ListView<RecentProjectInfo> recentProjects;

    static {
        Peach.getEventBus().addListener(WelcomeUI::onOpenedProject);
        Peach.getEventBus().addListener(WelcomeUI::onClosedProject);
    }

    public static void show() {
        stage = new Stage();
        stage.setScene(new Scene(new WelcomeUI()));
        stage.setTitle("🍑Peach");
        stage.setOnHidden(event -> {
            if (ProjectManager.getInstance().getOpenProjects().isEmpty()) {
                Peach.getInstance().exit();
            }
        });
        stage.show();
    }

    private static void onOpenedProject(ProjectWindowEvent.Opened event) {
        stage.hide();
    }

    private static void onClosedProject(ProjectWindowEvent.Closed event) {
        if (showWelcomeIfNoProjectOpened) {
            show();
        }
    }

    public WelcomeUI() {
        FXUtils.loadFXML(this, "ui/Welcome.fxml");

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
                } else {
                    setText(item.getName() + "\n" + item.getPath());
                }
            }
        });
        recentProjects.getItems().addAll(RecentProjectsManager.getInstance().getRecentProjects());
    }

    @FXML
    public void onNewProject() {
        NewProjectUI.show(getScene().getWindow());
    }

    @FXML
    public void onOpenProject() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(I18n.translate("ui.main.open_project"));
        File file = directoryChooser.showDialog(getScene().getWindow());
        if (file == null) return;
        ProjectManager.getInstance().openProject(file.toPath());
    }
}
