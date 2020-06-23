package com.github.mouse0w0.peach.ui.project;

import com.github.mouse0w0.eventbus.Listener;
import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.event.project.ProjectOpenedEvent;
import com.github.mouse0w0.peach.project.Project;

import java.util.HashMap;
import java.util.Map;

public class WindowManager {

    private static final WindowManager INSTANCE = new WindowManager();

    private final Map<Project, ProjectWindow> projectWindowMap = new HashMap<>();

    public static WindowManager getInstance() {
        return INSTANCE;
    }

    public WindowManager() {
        Peach.getEventBus().register(this);
    }

    public ProjectWindow getProjectWindow(Project project) {
        return projectWindowMap.get(project);
    }

    public ProjectWindow allocateProjectWindow(Project project) {
        ProjectWindow window = getProjectWindow(project);
        if (window != null) {
            return window;
        }

        window = new ProjectWindow(project);
        projectWindowMap.putIfAbsent(project, window);
        return window;
    }

    @Listener
    public void onOpenedProject(ProjectOpenedEvent event) {
        ProjectWindow window = allocateProjectWindow(event.getProject());
        window.getStage().show();
    }
}
