package com.github.mouse0w0.peach.window;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.project.Project;
import javafx.scene.Node;
import javafx.stage.Window;

import java.util.Collection;

public interface WindowManager {
    static WindowManager getInstance() {
        return Peach.getInstance().getService(WindowManager.class);
    }

    Collection<ProjectWindow> getWindows();

    ProjectWindow getWindow(Project project);

    ProjectWindow getWindow(Window window);

    Window getFocusedWindow();

    Node getFocusedNode();

    Project getFocusedProject();
}
