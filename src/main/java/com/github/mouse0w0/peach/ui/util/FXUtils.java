package com.github.mouse0w0.peach.ui.util;

import com.github.mouse0w0.i18n.I18n;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Window;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;

public final class FXUtils {

    public static void loadFXML(Object root, String location) {
        loadFXML(root, location, I18n.getResourceBundle());
    }


    public static void loadFXML(Object root, String location, ResourceBundle resources) {
        loadFXML(root, root, location, resources);
    }

    public static <T> T loadFXML(Object root, Object controller, String location) {
        return loadFXML(root, controller, location, I18n.getResourceBundle());
    }

    public static <T> T loadFXML(Object root, Object controller, String location, ResourceBundle resources) {
        FXMLLoader loader = new FXMLLoader();
        loader.setRoot(root);
        loader.setController(controller);
        ClassLoader classLoader = getClassLoaderOfCaller();
        loader.setClassLoader(classLoader);
        loader.setLocation(classLoader.getResource(location));
        loader.setResources(resources);
        loader.setCharset(StandardCharsets.UTF_8);
        try {
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Cannot load fxml", e);
        }
    }

    private static ClassLoader getClassLoaderOfCaller() {
        try {
            return Class.forName(Thread.currentThread().getStackTrace()[3].getClassName()).getClassLoader();
        } catch (ClassNotFoundException e) {
            return Thread.currentThread().getContextClassLoader();
        }
    }

    public static void addStyleSheet(Scene scene, String resourceName) {
        ClassLoader classLoader = getClassLoaderOfCaller();
        URL resource = classLoader.getResource(resourceName);
        if (resource == null) {
            throw new NullPointerException("Resource \"" + resourceName + "\" not found");
        }
        scene.getStylesheets().add(resource.toExternalForm());
    }

    public static void addStyleSheet(Parent parent, String resourceName) {
        ClassLoader classLoader = getClassLoaderOfCaller();
        URL resource = classLoader.getResource(resourceName);
        if (resource == null) {
            throw new NullPointerException("Resource \"" + resourceName + "\" not found");
        }
        parent.getStylesheets().add(resource.toExternalForm());
    }

    public static void hideWindow(Node node) {
        node.getScene().getWindow().hide();
    }

    public static void hideWindow(Scene scene) {
        scene.getWindow().hide();
    }

    private static final ListChangeListener<Node> DISABLE_TEXT_AREA_BLUR_LISTENER = new ListChangeListener<Node>() {
        @Override
        public void onChanged(Change<? extends Node> c) {
            ScrollPane scrollPane = (ScrollPane) c.getList().get(0);
            scrollPane.getChildrenUnmodifiable().addListener((ListChangeListener<Node>) change -> {
                while (change.next()) {
                    change.getAddedSubList().forEach(node -> node.setCache(false));
                    change.getRemoved().forEach(node -> node.setCache(true));
                }
            });
            c.getList().removeListener(this);
        }
    };

    public static void disableTextAreaBlur(TextArea textArea) {
        textArea.getChildrenUnmodifiable().addListener(DISABLE_TEXT_AREA_BLUR_LISTENER);
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

    public static Optional<Window> getFocusedWindow() {
        Iterator<Window> iterator = Window.impl_getWindows();
        while (iterator.hasNext()) {
            Window window = iterator.next();
            if (window.isFocused()) return Optional.of(window);
        }
        return Optional.empty();
    }

    private static Object TOOLTIP_BEHAVIOR;
    private static Field TOOLTIP_HOVERED_NODE;

    static {
        try {
            Field behaviorField = Tooltip.class.getDeclaredField("BEHAVIOR");
            behaviorField.setAccessible(true);
            TOOLTIP_BEHAVIOR = behaviorField.get(null);
            TOOLTIP_HOVERED_NODE = TOOLTIP_BEHAVIOR.getClass().getDeclaredField("hoveredNode");
            TOOLTIP_HOVERED_NODE.setAccessible(true);
        } catch (ReflectiveOperationException e) {
            TOOLTIP_BEHAVIOR = null;
            TOOLTIP_HOVERED_NODE = null;
        }
    }

    public static Optional<Node> getTooltipOwnerNode() {
        if (TOOLTIP_HOVERED_NODE == null) return Optional.empty();
        try {
            return Optional.ofNullable((Node) TOOLTIP_HOVERED_NODE.get(TOOLTIP_BEHAVIOR));
        } catch (IllegalAccessException e) {
            return Optional.empty();
        }
    }

    private FXUtils() {
    }
}
