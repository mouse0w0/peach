package com.github.mouse0w0.peach.ui.project;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.event.project.ProjectWindowEvent;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.project.ProjectManager;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ProjectWindow {

    private Project project;
    private Stage stage;
    private ProjectUI projectUI;

    public ProjectWindow(Project project) {
        this.project = project;
        this.stage = new Stage();
        this.projectUI = new ProjectUI(project);
        initStage();
    }

    private void initStage() {
        stage.setScene(new Scene(projectUI));
        stage.setTitle(project.getName());
        stage.setOnShown(event -> Peach.getEventBus().post(new ProjectWindowEvent.Opened(this)));
        stage.setOnHidden(event -> Peach.getEventBus().post(new ProjectWindowEvent.Closed(this)));
        stage.setOnCloseRequest(event -> {
            ProjectManager.getInstance().closeProject(project);
            event.consume();
        });
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
