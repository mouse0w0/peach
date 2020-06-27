package com.github.mouse0w0.peach.ui;

import com.github.mouse0w0.eventbus.Listener;
import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.event.project.ProjectOpenedEvent;
import com.github.mouse0w0.peach.project.ProjectManager;
import com.github.mouse0w0.peach.ui.project.NewProjectUI;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class WelcomeUI extends BorderPane {

    private static Stage WELCOME_UI_STAGE;

    @FXML
    public ListView<String> recentProject;

    static {
        Peach.getEventBus().addListener(WelcomeUI::onOpenedProject);
    }

    public static void show(Stage primaryStage) {
        WELCOME_UI_STAGE = primaryStage;
        WELCOME_UI_STAGE.setScene(new Scene(new WelcomeUI()));
        WELCOME_UI_STAGE.setTitle("🍑Peach");
        WELCOME_UI_STAGE.show();
    }

    @Listener
    public static void onOpenedProject(ProjectOpenedEvent event) {
        WELCOME_UI_STAGE.hide();
    }

    public WelcomeUI() {
        FXUtils.loadFXML(this, "ui/Welcome.fxml");
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
