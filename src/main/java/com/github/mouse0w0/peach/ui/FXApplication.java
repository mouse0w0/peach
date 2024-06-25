package com.github.mouse0w0.peach.ui;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.welcome.WelcomeWindow;
import com.sun.javafx.css.StyleManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FXApplication extends Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(FXApplication.class);

    @Override
    public void start(Stage primaryStage) {
        LOGGER.info("Starting JavaFX Application.");
        setUserAgentStylesheet(STYLESHEET_MODENA);
        StyleManager.getInstance().addUserAgentStylesheet(
                FXApplication.class.getResource("/style/UserAgent.css").toExternalForm());
        WelcomeWindow.showNow();
        LOGGER.info("Started JavaFX Application.");
    }

    @Override
    public void stop() {
        Peach.getInstance().exit(true);
    }
}
