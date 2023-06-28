package com.github.mouse0w0.peach.javafx.util;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.stage.Window;
import org.jetbrains.annotations.Nullable;

public class FocusUtils {
    private static Window focusedWindow;

    private static final InvalidationListener WINDOW_FOCUSED_LISTENER = observable -> {
        ReadOnlyBooleanProperty focusedProperty = (ReadOnlyBooleanProperty) observable;
        if (focusedProperty.get()) {
            focusedWindow = (Window) focusedProperty.getBean();
        }
    };

    static {
        ObservableList<Window> windows = Window.getWindows();
        windows.addListener((ListChangeListener<Window>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Window window : c.getAddedSubList()) {
                        window.focusedProperty().addListener(WINDOW_FOCUSED_LISTENER);
                    }
                }
                if (c.wasRemoved()) {
                    for (Window window : c.getRemoved()) {
                        window.focusedProperty().removeListener(WINDOW_FOCUSED_LISTENER);
                    }
                }
            }
        });
        for (Window window : windows) {
            if (window.isFocused()) {
                focusedWindow = window;
                break;
            }
        }
    }

    @Nullable
    public static Window getFocusedWindow() {
        return focusedWindow;
    }

    @Nullable
    public static Node getFocusedNode() {
        return focusedWindow != null ? focusedWindow.getScene().getFocusOwner() : null;
    }
}
