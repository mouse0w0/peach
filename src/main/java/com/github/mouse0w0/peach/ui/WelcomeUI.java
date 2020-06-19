package com.github.mouse0w0.peach.ui;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.project.ProjectManager;
import com.github.mouse0w0.peach.ui.project.NewProject;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class WelcomeUI extends BorderPane {

    public WelcomeUI() {
        FXUtils.loadFXML(this, "ui/Welcome.fxml");
    }

    @FXML
    public void onNewProject() {
        NewProject newProject = new NewProject();
        Stage stage = new Stage();
        stage.setScene(new Scene(newProject));
        stage.setTitle(I18n.translate("ui.new_project.title"));
        stage.initOwner(getScene().getWindow());
        stage.showAndWait();
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
