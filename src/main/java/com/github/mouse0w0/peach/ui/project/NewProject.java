package com.github.mouse0w0.peach.ui.project;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class NewProject extends BorderPane {

    @FXML
    public TextField projectName;
    @FXML
    public TextField projectPath;

    public NewProject() {
        FXUtils.loadFXML(this, "ui/NewProject.fxml");
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

    }

    @FXML
    public void onCancel() {
        getScene().getWindow().hide();
    }
}
