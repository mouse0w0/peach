package com.github.mouse0w0.peach.window;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionHolder;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.project.ProjectLifecycleListener;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import com.github.mouse0w0.peach.ui.util.FocusUtils;
import com.sun.javafx.scene.control.ContextMenuContent;
import com.sun.javafx.scene.control.MenuBarButton;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;

public class WindowManagerImpl implements WindowManager {
    private final Map<Project, ProjectWindow> windowMap = new WeakHashMap<>();
    private final Map<Window, ProjectWindow> fxWindowToWindowMap = new WeakHashMap<>();

    public WindowManagerImpl() {
        Window.getWindows().addListener((ListChangeListener<Window>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Window window : c.getAddedSubList()) {
                        if (window instanceof ContextMenu contextMenu) {
                            Node ownerNode = contextMenu.getOwnerNode();
                            if (window instanceof ActionHolder) {
                                applyStatusBarInfo(contextMenu);
                            } else if (ownerNode instanceof MenuBarButton button && button.menu instanceof ActionHolder) {
                                applyStatusBarInfo(contextMenu);
                            } else if (ownerNode instanceof ContextMenuContent.MenuItemContainer container && container.getItem() instanceof ActionHolder) {
                                applyStatusBarInfo(contextMenu);
                            }
                        }
                    }
                }
            }
        });
    }

    private static final InvalidationListener HOVER_LISTENER = observable -> {
        ReadOnlyBooleanProperty hoverProperty = (ReadOnlyBooleanProperty) observable;
        if (hoverProperty.get()) {
            Node node = (Node) hoverProperty.getBean();
            MenuItem menuItem = FXUtils.getProperty(node, MenuItem.class);
            assert menuItem != null;
            Action action = ((ActionHolder) menuItem).getAction();
            StatusBarInfo.getFocusedInstance().setText(action.getDescription());
        } else {
            StatusBarInfo.getFocusedInstance().setText(null);
        }
    };

    private static void applyStatusBarInfo(ContextMenu contextMenu) {
        Parent root = contextMenu.getScene().getRoot();
        for (Node node : root.lookupAll(".menu-item")) {
            MenuItem menuItem = FXUtils.getProperty(node, MenuItem.class);
            if (menuItem instanceof ActionHolder actionHolder && actionHolder.getAction().getDescription() != null) {
                node.hoverProperty().addListener(HOVER_LISTENER);
            }
        }
    }

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

    @Override
    public Project getFocusedProject() {
        ProjectWindow projectWindow = getFocusedWindow();
        return projectWindow != null ? projectWindow.getProject() : null;
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
