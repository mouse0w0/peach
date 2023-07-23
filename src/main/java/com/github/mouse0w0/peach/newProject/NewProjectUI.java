package com.github.mouse0w0.peach.newProject;

import com.github.mouse0w0.peach.dialog.Alert;
import com.github.mouse0w0.peach.javafx.control.FilePicker;
import com.github.mouse0w0.peach.javafx.util.FXUtils;
import com.github.mouse0w0.peach.l10n.AppL10n;
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
        stage.setTitle(AppL10n.localize("dialog.newProject.title"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public NewProjectUI() {
        FXUtils.loadFXML(this, "ui/project/NewProject.fxml", AppL10n.getResourceBundle());
        FileChooserHelper.getInstance().register(path, "newProject");
    }

    @FXML
    public void onFinish() {
        if (doCreateProject()) {
            FXUtils.hideWindow(this);
        }
    }

    private boolean doCreateProject() {
        final Path path = this.path.getPath();
        if (path == null) {
            return false; // TODO: show alert
        }
        if (FileUtils.notEmptyDirectory(path)) {
            if (!Alert.confirm(AppL10n.localize("dialog.newProject.notEmpty", FileUtils.getFileName(path)))) {
                return false;
            }
        }
        ProjectManager.getInstance().createProject(name.getText(), path);
        return true;
    }

    @FXML
    public void onCancel() {
        FXUtils.hideWindow(this);
    }
}
