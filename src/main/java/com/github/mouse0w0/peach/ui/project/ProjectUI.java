package com.github.mouse0w0.peach.ui.project;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.forge.ForgeProjectDataKeys;
import com.github.mouse0w0.peach.forge.compiler.ForgeCompiler;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.project.ProjectManager;
import com.github.mouse0w0.peach.ui.forge.ModInfoUI;
import com.github.mouse0w0.peach.ui.forge.NewModElementUI;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class ProjectUI extends BorderPane {

    private final Project project;

    @FXML
    private TabPane content;

    public ProjectUI(Project project) {
        this.project = project;
        FXUtils.loadFXML(this, "ui/project/Project.fxml");
    }

    public TabPane getContent() {
        return content;
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
        ModInfoUI.show(project.getData(ForgeProjectDataKeys.MOD_INFO_FILE), getScene().getWindow());
    }

    @FXML
    private void onNewElement() {
        NewModElementUI.show(getScene().getWindow());
    }

    @FXML
    private void doBuild() {
        new ForgeCompiler(project.getPath(), project.getPath().resolve("build")).run();
    }
}
