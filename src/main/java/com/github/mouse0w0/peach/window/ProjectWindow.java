package com.github.mouse0w0.peach.window;

import com.github.mouse0w0.peach.javafx.control.ViewPane;
import com.github.mouse0w0.peach.project.Project;
import javafx.scene.control.Tab;
import javafx.stage.Stage;

public interface ProjectWindow {
    Project getProject();

    Stage getStage();

    ViewPane getViewPane();

    StatusBar getStatusBar();

    void show();

    void hide();

    void requestFocus();

    void openTab(Tab tab);

    void addTab(Tab tab);

    void selectTab(Tab tab);

    void removeTab(Tab tab);
}
