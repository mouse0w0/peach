package com.github.mouse0w0.peach.ui.project;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.forge.ForgeProjectInfo;
import com.github.mouse0w0.peach.forge.generator.ForgeCompiler;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.project.ProjectManager;
import com.github.mouse0w0.peach.ui.forge.ModInfoUI;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class ProjectUI extends BorderPane {

    private final Project project;

    public ProjectUI(Project project) {
        this.project = project;
        FXUtils.loadFXML(this, "ui/project/Project.fxml");
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

    @FXML
    public void onExit() {
        Peach.getInstance().exit();
    }

    @FXML
    public void doOpenSettings() {

    }

    @FXML
    public void doOpenProjectSettings() {
        ModInfoUI.show(project.getData(ForgeProjectInfo.KEY), getScene().getWindow());
    }

    @FXML
    public void doBuild() {
        new ForgeCompiler(project.getPath(), project.getPath().resolve("build")).run();
    }
}
