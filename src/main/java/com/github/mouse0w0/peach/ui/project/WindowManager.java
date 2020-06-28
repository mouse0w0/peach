package com.github.mouse0w0.peach.ui.project;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.event.project.ProjectEvent;
import com.github.mouse0w0.peach.project.Project;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class WindowManager {

    private final Map<Project, ProjectWindow> projectWindowMap = new HashMap<>();

    public static WindowManager getInstance() {
        return Peach.getInstance().getService(WindowManager.class);
    }

    public WindowManager() {
        Peach.getEventBus().addListener(this::onOpenedProject);
    }

    public Collection<ProjectWindow> getProjectWindows() {
        return projectWindowMap.values();
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

    private void onOpenedProject(ProjectEvent.Opened event) {
        ProjectWindow window = allocateProjectWindow(event.getProject());
        window.getStage().show();
    }
}
