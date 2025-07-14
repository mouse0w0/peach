package com.github.mouse0w0.peach.style;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;

public final class StyleManagerImpl implements StyleManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(StyleManagerImpl.class);

    @Override
    public void reloadUserAgentStylesheet() {
        if (Platform.isFxApplicationThread()) {
            reloadUserAgentStylesheet0();
        } else {
            Platform.runLater(this::reloadUserAgentStylesheet0);
        }
    }

    private void reloadUserAgentStylesheet0() {
        Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);

        var peer = com.sun.javafx.css.StyleManager.getInstance();
        peer.addUserAgentStylesheet(getStyleURL("Variable"));
        peer.addUserAgentStylesheet(getStyleURL("UserAgent"));
    }

    @Override
    public void apply(Scene scene, String styleId) {
        addStylesheet(scene.getStylesheets(), styleId);
    }

    @Override
    public void apply(Parent parent, String styleId) {
        addStylesheet(parent.getStylesheets(), styleId);
    }

    private static void addStylesheet(List<String> stylesheets, String styleId) {
        String file = "/style/" + styleId + ".css";
        URL url = StyleManagerImpl.class.getResource(file);
        if (url == null) {
            LOGGER.debug("Not found stylesheet file, skip it. id={}, file={}", styleId, file);
            return;
        }
        stylesheets.add(url.toExternalForm());
    }

    private static String getStyleURL(String styleId) {
        return StyleManagerImpl.class.getResource("/style/" + styleId + ".css").toExternalForm();
    }
}
