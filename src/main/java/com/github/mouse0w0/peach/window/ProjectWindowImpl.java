package com.github.mouse0w0.peach.window;

import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.data.DataManager;
import com.github.mouse0w0.peach.data.DataProvider;
import com.github.mouse0w0.peach.icon.AppIcon;
import com.github.mouse0w0.peach.javafx.control.ViewPane;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.project.ProjectManager;
import com.github.mouse0w0.peach.project.service.WindowStateManager;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;

public class ProjectWindowImpl implements ProjectWindow, DataProvider {
    private final Project project;
    private final Scene scene;
    private final ProjectRootPane root;
    private final Stage stage;

    ProjectWindowImpl(Project project) {
        this.project = project;
        this.root = new ProjectRootPane(project);
        this.scene = new Scene(root);
        this.stage = createStage();
        DataManager.getInstance().registerDataProvider(stage, this);
        WindowStateManager.getInstance(project).register(stage, "MainWindow");
    }

    private Stage createStage() {
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(project.getName());
        stage.getIcons().setAll(Icons.Peach.getImage());
        stage.setOnShown(this::onShown);
        stage.setOnHidden(this::onHidden);
        return stage;
    }

    @Override
    public Project getProject() {
        return project;
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public ViewPane getViewPane() {
        return root.getViewPane();
    }

    @Override
    public StatusBar getStatusBar() {
        return root.getStatusBar();
    }

    @Override
    public void show() {
        stage.show();
    }

    @Override
    public void hide() {
        stage.hide();
    }

    @Override
    public void requestFocus() {
        stage.requestFocus();
    }

    @Override
    public void openTab(Tab tab) {
        addTab(tab);
        selectTab(tab);
    }

    @Override
    public void addTab(Tab tab) {
        root.getTabPane().getTabs().add(tab);
    }

    @Override
    public void selectTab(Tab tab) {
        root.getTabPane().getSelectionModel().select(tab);
    }

    @Override
    public void removeTab(Tab tab) {
        root.getTabPane().getTabs().remove(tab);
    }

    @Override
    public Object getData(@NotNull String key) {
        if (DataKeys.PROJECT.is(key)) {
            return project;
        }
        return null;
    }

    private void onShown(WindowEvent event) {
        project.getMessageBus().getPublisher(ProjectWindowListener.TOPIC).windowShown(this);
    }

    private void onHidden(WindowEvent event) {
        project.getMessageBus().getPublisher(ProjectWindowListener.TOPIC).windowHidden(this);
        ProjectManager.getInstance().closeProject(project);
    }
}
