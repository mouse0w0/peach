package com.github.mouse0w0.peach.window;

import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.project.ProjectLifecycleListener;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import com.github.mouse0w0.peach.ui.util.FocusUtils;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;

public final class WindowManagerImpl implements WindowManager {
    private final Map<Project, ProjectWindow> windowMap = new WeakHashMap<>();
    private final Map<Window, ProjectWindow> fxWindowToWindowMap = new WeakHashMap<>();

    @Override
    public Collection<ProjectWindow> getWindows() {
        return windowMap.values();
    }

    @Override
    public ProjectWindow getWindow(Project project) {
        return windowMap.get(project);
    }

    @Override
    public ProjectWindow getWindow(Window window) {
        ProjectWindow projectWindow = fxWindowToWindowMap.get(window);
        if (projectWindow != null) return projectWindow;

        Window current = window;
        do {
            if (current instanceof Stage) current = ((Stage) current).getOwner();
            else if (current instanceof PopupWindow) current = ((PopupWindow) current).getOwnerWindow();
            else break;

            projectWindow = fxWindowToWindowMap.get(current);
        } while (current != null);
        fxWindowToWindowMap.put(window, projectWindow);
        return projectWindow;
    }

    @Override
    public ProjectWindow getFocusedWindow() {
        for (Window w = FocusUtils.getFocusedWindow(); w != null; w = FXUtils.getOwner(w)) {
            ProjectWindow projectWindow = fxWindowToWindowMap.get(w);
            if (projectWindow != null) {
                return projectWindow;
            }
        }
        return null;
    }

    public ProjectWindow getOrCreateWindow(Project project) {
        ProjectWindow window = getWindow(project);
        if (window != null) {
            return window;
        }

        window = new ProjectWindowImpl(project);
        windowMap.putIfAbsent(project, window);
        fxWindowToWindowMap.put(window.getStage(), window);
        return window;
    }

    public static final class Listener implements ProjectLifecycleListener {
        @Override
        public void projectOpened(Project project) {
            ((WindowManagerImpl) WindowManager.getInstance()).getOrCreateWindow(project).show();
        }

        @Override
        public void projectClosingBeforeSave(Project project) {
            WindowManager.getInstance().getWindow(project).hide();
        }
    }
}
