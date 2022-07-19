package com.github.mouse0w0.peach.window;

import com.github.mouse0w0.eventbus.Order;
import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.data.DataManager;
import com.github.mouse0w0.peach.event.project.ProjectEvent;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.view.ViewManager;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;

public class WindowManager {

    private final Map<Project, ProjectWindow> windowMap = new WeakHashMap<>();
    private final Map<Window, ProjectWindow> stageToWindowMap = new WeakHashMap<>();

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

    public static WindowManager getInstance() {
        return Peach.getInstance().getService(WindowManager.class);
    }

    public WindowManager() {
        Peach.getEventBus().addListener(Order.LAST, this::onOpenedProject);
        Peach.getEventBus().addListener(Order.LAST, this::onClosingBeforeSaveProject);
        Window.getWindows().addListener(new ListChangeListener<Window>() {
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

    public Window getFocusedWindow() {
        return focusedWindow;
    }

    public Node getFocusedNode() {
        if (focusedWindow == null) return null;
        return focusedWindow.getScene().getFocusOwner();
    }

    public Project getFocusedProject() {
        if (focusedWindow == null) return null;
        return DataKeys.PROJECT.get(DataManager.getInstance().getDataContext(focusedWindow));
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
