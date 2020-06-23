package com.github.mouse0w0.peach.ui;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.project.ProjectManager;
import com.github.mouse0w0.peach.ui.project.NewProjectUI;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class WelcomeUI extends BorderPane {

    @FXML
    public ListView<String> recentProject;

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
