package com.github.mouse0w0.peach.newProject;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.dialog.Alert;
import com.github.mouse0w0.peach.javafx.FXUtils;
import com.github.mouse0w0.peach.javafx.control.FilePicker;
import com.github.mouse0w0.peach.project.ProjectManager;
import com.github.mouse0w0.peach.project.service.FileChooserHelper;
import com.github.mouse0w0.peach.util.FileUtils;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.nio.file.Path;

public class NewProjectUI extends BorderPane {

    @FXML
    private TextField name;
    @FXML
    private FilePicker path;

    public static void show() {
        NewProjectUI newProject = new NewProjectUI();
        Stage stage = new Stage();
        stage.setScene(new Scene(newProject));
        stage.setTitle(I18n.translate("dialog.newProject.title"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public NewProjectUI() {
        FXUtils.loadFXML(this, "ui/project/NewProject.fxml");
        FileChooserHelper.getInstance().register(path, "newProject");
    }

    @FXML
    public void onFinish() {
        FXUtils.hideWindow(this);
        doCreateProject();
    }

    private void doCreateProject() {
        Path folder = path.toPath();
        if (folder == null) return; // TODO: show alert
        if (FileUtils.isNotEmpty(folder)) {
            if (!Alert.confirm(I18n.translate("dialog.newProject.notEmpty"))) return;
        }
        ProjectManager.getInstance().createProject(name.getText(), folder);
    }

    @FXML
    public void onCancel() {
        FXUtils.hideWindow(this);
    }
}
