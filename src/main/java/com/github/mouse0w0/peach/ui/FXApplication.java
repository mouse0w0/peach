package com.github.mouse0w0.peach.ui;

import com.github.mouse0w0.peach.welcome.WelcomeUI;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FXApplication extends Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(FXApplication.class);

    @Override
    public void start(Stage primaryStage) {
        LOGGER.info("Starting JavaFX Application.");
        WelcomeUI.show();
        LOGGER.info("Started JavaFX Application.");
    }
}
