package com.github.mouse0w0.peach.ui.project;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.mcmod.compiler.CompilerImpl;
import com.github.mouse0w0.peach.mcmod.data.McModDataKeys;
import com.github.mouse0w0.peach.mcmod.ui.ModInfoUI;
import com.github.mouse0w0.peach.mcmod.ui.NewModElementUI;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.project.ProjectManager;
import com.github.mouse0w0.peach.ui.newProject.NewProjectUI;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;

import java.io.File;

class ProjectUI extends BorderPane {

    private final Project project;

    @FXML
    private TabPane tabPane;

    public ProjectUI(Project project) {
        this.project = project;
        FXUtils.loadFXML(this, "ui/project/Project.fxml");
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    @FXML
    private void onNewProject() {
        NewProjectUI.show(getScene().getWindow());
    }

    @FXML
    private void onOpenProject() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(I18n.translate("ui.main.open_project"));
        File file = directoryChooser.showDialog(getScene().getWindow());
        if (file == null) return;
        ProjectManager.getInstance().openProject(file.toPath());
    }

    @FXML
    private void onExit() {
        Peach.getInstance().exit();
    }

    @FXML
    private void doOpenSettings() {
    }

    @FXML
    private void doOpenProjectSettings() {
        ModInfoUI.show(project.getData(McModDataKeys.MOD_SETTINGS), getScene().getWindow());
    }

    @FXML
    private void onNewElement() {
        NewModElementUI.show(getScene().getWindow());
    }

    @FXML
    private void doBuild() {
        new CompilerImpl(project.getPath(), project.getPath().resolve("build")).run();
    }
}
