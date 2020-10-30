package com.github.mouse0w0.peach.ui.newProject;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.project.ProjectManager;
import com.github.mouse0w0.peach.ui.util.Alerts;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import com.github.mouse0w0.peach.util.FileUtils;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NewProjectUI extends BorderPane {

    @FXML
    public TextField projectName;
    @FXML
    public TextField projectPath;

    public static void show(Window window) {
        NewProjectUI newProject = new NewProjectUI();
        Stage stage = new Stage();
        stage.setScene(new Scene(newProject));
        stage.setTitle(I18n.translate("ui.new_project.title"));
        stage.initOwner(window);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public NewProjectUI() {
        FXUtils.loadFXML(this, "ui/project/NewProject.fxml");
        projectName.setText("untitled");
    }

    @FXML
    public void onChooseProjectPath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(I18n.translate("ui.new_project.choose_path"));
        File file = directoryChooser.showDialog(getScene().getWindow());
        if (file != null) projectPath.setText(file.toString());
    }

    @FXML
    public void onFinish() {
        FXUtils.hideWindow(this);
        doCreateProject();
    }

    private void doCreateProject() {
        Path path = Paths.get(projectPath.getText());
        try {
            if (FileUtils.isNotEmpty(path) && !Alerts.confirm(I18n.translate("ui.new_project.not_empty"))) return;
        } catch (IOException ignored) {
        }
        ProjectManager.getInstance().createProject(projectName.getText(), path);
    }

    @FXML
    public void onCancel() {
        FXUtils.hideWindow(this);
    }
}
