package com.github.mouse0w0.peach.ui.util;

import com.github.mouse0w0.i18n.I18n;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public final class FXUtils {

    public static void loadFXML(Object root, String location) {
        loadFXML(root, location, I18n.getResourceBundle());
    }

    public static void loadFXML(Object root, String location, ResourceBundle resources) {
        FXMLLoader loader = new FXMLLoader();
        loader.setRoot(root);
        loader.setController(root);
        ClassLoader classLoader = getClassLoader(getCallerClass());
        loader.setClassLoader(classLoader);
        loader.setLocation(classLoader.getResource(location));
        loader.setResources(resources);
        loader.setCharset(StandardCharsets.UTF_8);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Cannot load fxml", e);
        }
    }

    private static ClassLoader getClassLoader(Class<?> clazz) {
        return clazz != null ? clazz.getClassLoader() : Thread.currentThread().getContextClassLoader();
    }

    private static Class<?> getCallerClass() {
        try {
            return Class.forName(Thread.currentThread().getStackTrace()[3].getClassName());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static void hideWindow(Node node) {
        node.getScene().getWindow().hide();
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

    private FXUtils() {
    }
}
