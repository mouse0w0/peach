package com.github.mouse0w0.peach.ui.project;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.event.project.ProjectWindowEvent;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.project.ProjectManager;
import com.github.mouse0w0.peach.project.service.WindowStateManager;
import com.github.mouse0w0.peach.ui.icon.Icons;
import com.github.mouse0w0.viewpane.ViewPane;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.stage.Stage;

public class ProjectWindow {

    private final Project project;
    private final Scene scene;
    private final ProjectRootPane root;
    private final Stage stage;

    ProjectWindow(Project project) {
        this.project = project;
        this.root = new ProjectRootPane(project);
        this.scene = new Scene(root);
        this.stage = createStage();
    }

    private Stage createStage() {
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(project.getName());
        stage.getIcons().setAll(Icons.Peach_16x);
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
        WindowStateManager.getInstance(project).register(stage, "MainWindow");
        return stage;
    }

    public Project getProject() {
        return project;
    }

    public Stage getStage() {
        return stage;
    }

    public ViewPane getViewPane() {
        return root.getViewPane();
    }

    public StatusBar getStatusBar() {
        return root.getStatusBar();
    }

    public void openTab(Tab tab) {
        addTab(tab);
        selectTab(tab);
    }

    public void addTab(Tab tab) {
        root.getTabPane().getTabs().add(tab);
    }

    public void selectTab(Tab tab) {
        root.getTabPane().getSelectionModel().select(tab);
    }

    public void removeTab(Tab tab) {
        root.getTabPane().getTabs().remove(tab);
    }

    public void requestFocus() {
        stage.requestFocus();
    }
}
