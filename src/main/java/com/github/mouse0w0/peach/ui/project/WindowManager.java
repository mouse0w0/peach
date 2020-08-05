package com.github.mouse0w0.peach.ui.project;

import com.github.mouse0w0.eventbus.Order;
import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.event.project.ProjectEvent;
import com.github.mouse0w0.peach.project.Project;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class WindowManager {

    private final Map<Project, ProjectWindow> projectWindowMap = new HashMap<>();

    private ProjectWindow focusedWindow;

    public static WindowManager getInstance() {
        return Peach.getInstance().getService(WindowManager.class);
    }

    public WindowManager() {
        Peach.getEventBus().addListener(Order.LAST, this::onOpenedProject);
        Peach.getEventBus().addListener(Order.LAST, this::onClosedProject);
    }

    public Collection<ProjectWindow> getProjectWindows() {
        return projectWindowMap.values();
    }

    public ProjectWindow getWindow(Project project) {
        return projectWindowMap.get(project);
    }

    public ProjectWindow allocateWindow(Project project) {
        ProjectWindow window = getWindow(project);
        if (window != null) {
            return window;
        }

        window = new ProjectWindow(project);
        projectWindowMap.putIfAbsent(project, window);
        return window;
    }

    public ProjectWindow getFocusedWindow() {
        return focusedWindow;
    }

    void setFocusedWindow(ProjectWindow window) {
        this.focusedWindow = window;
    }

    private void onOpenedProject(ProjectEvent.Opened event) {
        ProjectWindow window = allocateWindow(event.getProject());
        window.getWindow().show();
    }

    private void onClosedProject(ProjectEvent.Closed event) {
        ProjectWindow window = getWindow(event.getProject());
        window.getWindow().hide();
    }
}
