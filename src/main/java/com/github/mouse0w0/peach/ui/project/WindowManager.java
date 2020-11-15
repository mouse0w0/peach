package com.github.mouse0w0.peach.ui.project;

import com.github.mouse0w0.eventbus.Order;
import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.event.project.ProjectEvent;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.view.ViewManager;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;

public class WindowManager {

    private final Map<Project, ProjectWindow> windowMap = new WeakHashMap<>();
    private final Map<Window, ProjectWindow> stageToWindowMap = new WeakHashMap<>();

    private ProjectWindow focusedWindow;

    public static WindowManager getInstance() {
        return Peach.getInstance().getService(WindowManager.class);
    }

    public WindowManager() {
        Peach.getEventBus().addListener(Order.LAST, this::onOpenedProject);
        Peach.getEventBus().addListener(Order.LAST, this::onClosingBeforeSaveProject);
    }

    public Collection<ProjectWindow> getWindows() {
        return windowMap.values();
    }

    public ProjectWindow getWindow(Project project) {
        return windowMap.get(project);
    }

    public ProjectWindow getWindow(Window window) {
        ProjectWindow projectWindow = stageToWindowMap.get(window);
        if (projectWindow != null) return projectWindow;

        Window current = window;
        do {
            if (current instanceof Stage) current = ((Stage) current).getOwner();
            else if (current instanceof PopupWindow) current = ((PopupWindow) current).getOwnerWindow();
            else break;

            projectWindow = stageToWindowMap.get(current);
        } while (current != null);
        stageToWindowMap.put(window, projectWindow);
        return projectWindow;
    }

    public Stage getStage(Project project) {
        return getWindow(project).getStage();
    }

    public StatusBar getStatusBar(Project project) {
        return getWindow(project).getStatusBar();
    }

    public ProjectWindow allocateWindow(Project project) {
        ProjectWindow window = getWindow(project);
        if (window != null) {
            return window;
        }

        window = new ProjectWindow(project);
        windowMap.putIfAbsent(project, window);
        stageToWindowMap.put(window.getStage(), window);

        ViewManager.getInstance(project).initialize(window);
        return window;
    }

    public ProjectWindow getFocusedWindow() {
        return focusedWindow;
    }

    public Project getFocusedProject() {
        return focusedWindow != null ? focusedWindow.getProject() : null;
    }

    void setFocusedWindow(ProjectWindow window) {
        this.focusedWindow = window;
    }

    private void onOpenedProject(ProjectEvent.Opened event) {
        ProjectWindow window = allocateWindow(event.getProject());
        window.getStage().show();
    }

    private void onClosingBeforeSaveProject(ProjectEvent.ClosingBeforeSave event) {
        ProjectWindow window = getWindow(event.getProject());
        window.getStage().hide();
    }
}
