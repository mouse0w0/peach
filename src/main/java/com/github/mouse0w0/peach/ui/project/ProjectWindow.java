package com.github.mouse0w0.peach.ui.project;

import com.github.mouse0w0.peach.project.Project;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ProjectWindow {

    private Project project;
    private Stage stage;
    private ProjectUI projectUI;

    public ProjectWindow(Project project) {
        this.project = project;
        this.stage = new Stage();
        this.projectUI = new ProjectUI();
        initStage();
    }

    private void initStage() {
        stage.setScene(new Scene(projectUI));
        stage.setTitle(project.getName());
    }

    public Project getProject() {
        return project;
    }

    public Stage getStage() {
        return stage;
    }

    public ProjectUI getProjectUI() {
        return projectUI;
    }
}
