package com.github.mouse0w0.peach.ui.project;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.event.project.ProjectWindowEvent;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.project.ProjectManager;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.stage.Stage;

public class ProjectWindow {

    private Project project;
    private Stage stage;
    private ProjectUI content;

    public ProjectWindow(Project project) {
        this.project = project;
        this.stage = new Stage();
        this.content = new ProjectUI(project);
        initStage();
    }

    private void initStage() {
        stage.setScene(new Scene(content));
        stage.setTitle(project.getName());
        stage.setOnShown(event -> Peach.getEventBus().post(new ProjectWindowEvent.Opened(this)));
        stage.setOnHidden(event -> Peach.getEventBus().post(new ProjectWindowEvent.Closed(this)));
        stage.focusedProperty().addListener(observable -> {
            if (stage.isFocused()) {
                WindowManager.getInstance().setFocusedWindow(this);
            }
        });
        stage.setOnCloseRequest(event -> {
            ProjectManager.getInstance().closeProject(project);
            event.consume();
        });
    }

    public Project getProject() {
        return project;
    }

    public Stage getWindow() {
        return stage;
    }

    public void openTab(Tab tab) {
        addTab(tab);
        selectTab(tab);
    }

    public void addTab(Tab tab) {
        content.getTabPane().getTabs().add(tab);
    }

    public void selectTab(Tab tab) {
        content.getTabPane().getSelectionModel().select(tab);
    }

    public void removeTab(Tab tab) {
        content.getTabPane().getTabs().remove(tab);
    }

    public void requestFocus() {
        stage.requestFocus();
    }
}
