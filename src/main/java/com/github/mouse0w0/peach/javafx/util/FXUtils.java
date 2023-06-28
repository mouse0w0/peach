package com.github.mouse0w0.peach.javafx.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;

public final class FXUtils {
    public static void loadFXML(Object root, String location, ResourceBundle resources) {
        loadFXML(root, root, getCallerClassLoader(), location, resources);
    }

    public static <T> T loadFXML(Object root, Object controller, String location, ResourceBundle resourceBundle) {
        return loadFXML(root, controller, getCallerClassLoader(), location, resourceBundle);
    }

    public static <T> T loadFXML(Object root, Object controller, ClassLoader classLoader, String location, ResourceBundle resourceBundle) {
        URL resource = classLoader.getResource(location);
        if (resource == null) {
            throw new IllegalArgumentException("Not found resource, location=" + location);
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setRoot(root);
        loader.setController(controller);
        loader.setClassLoader(classLoader);
        loader.setLocation(resource);
        loader.setResources(resourceBundle);
        loader.setCharset(StandardCharsets.UTF_8);
        try {
            return loader.load();
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot load fxml", e);
        }
    }

    private static ClassLoader getCallerClassLoader() {
        try {
            // new Throwable().getStackTrace() faster than Thread.currentThread().getStackTrace().
            return Class.forName(new Throwable().getStackTrace()[2].getClassName()).getClassLoader();
        } catch (ClassNotFoundException e) {
            return Thread.currentThread().getContextClassLoader();
        }
    }

    public static void addStylesheet(Scene scene, String location) {
        ClassLoader classLoader = getCallerClassLoader();
        URL resource = classLoader.getResource(location);
        if (resource == null) {
            throw new IllegalArgumentException("Not found resource, location=" + location);
        }
        scene.getStylesheets().add(resource.toExternalForm());
    }

    public static void addStylesheet(Parent parent, String location) {
        ClassLoader classLoader = getCallerClassLoader();
        URL resource = classLoader.getResource(location);
        if (resource == null) {
            throw new IllegalArgumentException("Not found resource, location=" + location);
        }
        parent.getStylesheets().add(resource.toExternalForm());
    }

    public static Window getOwner(Window window) {
        if (window instanceof Stage) {
            return ((Stage) window).getOwner();
        } else if (window instanceof PopupWindow) {
            return ((PopupWindow) window).getOwnerWindow();
        } else {
            throw new UnsupportedOperationException("getOwner does not support " + window.getClass().getName());
        }
    }

    public static void hideWindow(Node node) {
        node.getScene().getWindow().hide();
    }

    public static void hideWindow(Scene scene) {
        scene.getWindow().hide();
    }

    public static void setFixedSize(Region region, double width, double height) {
        region.setMinSize(width, height);
        region.setPrefSize(width, height);
        region.setMaxSize(width, height);
    }

    public static void setFitToAnchorPane(Node node) {
        AnchorPane.setTopAnchor(node, 0D);
        AnchorPane.setLeftAnchor(node, 0D);
        AnchorPane.setBottomAnchor(node, 0D);
        AnchorPane.setRightAnchor(node, 0D);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getProperty(Node node, Object key) {
        return node.hasProperties() ? (T) node.getProperties().get(key) : null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T computeProperty(Node node, Object key, Function<Object, Object> mappingFunction) {
        return (T) node.getProperties().computeIfAbsent(key, mappingFunction);
    }

    public static Optional<Window> getFocusedWindow() {
        for (Window window : Window.getWindows()) {
            if (window.isFocused()) {
                return Optional.of(window);
            }
        }
        return Optional.empty();
    }

    private FXUtils() {
    }
}
