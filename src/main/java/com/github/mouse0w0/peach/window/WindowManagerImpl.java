package com.github.mouse0w0.peach.window;

import com.github.mouse0w0.peach.javafx.FXUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.project.ProjectLifecycleListener;
import com.github.mouse0w0.peach.view.ViewManager;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;

public class WindowManagerImpl implements WindowManager {

    private final Map<Project, ProjectWindow> windowMap = new WeakHashMap<>();
    private final Map<Window, ProjectWindow> fxWindowToWindowMap = new WeakHashMap<>();

    private final InvalidationListener windowFocusedListener = new InvalidationListener() {
        @Override
        public void invalidated(Observable observable) {
            ReadOnlyBooleanProperty property = (ReadOnlyBooleanProperty) observable;
            if (property.get()) {
                focusedWindow = (Window) property.getBean();
            }
        }
    };

    private Window focusedWindow;

    public WindowManagerImpl() {
        ObservableList<Window> windows = Window.getWindows();
        windows.addListener(new ListChangeListener<>() {
            @Override
            public void onChanged(Change<? extends Window> c) {
                while (c.next()) {
                    if (c.wasAdded()) {
                        for (Window window : c.getAddedSubList()) {
                            window.focusedProperty().addListener(windowFocusedListener);
                        }
                    }
                    if (c.wasRemoved()) {
                        for (Window window : c.getRemoved()) {
                            window.focusedProperty().removeListener(windowFocusedListener);
                        }
                    }
                }
                c.reset();
            }
        });
        for (Window window : windows) {
            if (window.isFocused()) {
                focusedWindow = window;
            }
        }
    }

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
    public Window getFocusedWindow() {
        return focusedWindow;
    }

    @Override
    public Node getFocusedNode() {
        if (focusedWindow == null) return null;
        return focusedWindow.getScene().getFocusOwner();
    }

    @Override
    public Project getFocusedProject() {
        for (Window w = focusedWindow; w != null; w = FXUtils.getOwner(w)) {
            ProjectWindow projectWindow = fxWindowToWindowMap.get(w);
            if (projectWindow != null) {
                return projectWindow.getProject();
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

        ViewManager.getInstance(project).initialize(window);
        return window;
    }

    public static final class Listener implements ProjectLifecycleListener {
        @Override
        public void projectOpened(Project project) {
            ((WindowManagerImpl) WindowManager.getInstance()).getOrCreateWindow(project).show();
        }

        @Override
        public void projectClosingBeforeSave(Project project) {

        }

        @Override
        public void projectClosing(Project project) {
            WindowManager.getInstance().getWindow(project).hide();
        }

        @Override
        public void projectClosed(Project project) {

        }
    }
}
