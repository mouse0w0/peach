package com.github.mouse0w0.peach.ui;

import javafx.application.Application;
import javafx.stage.Stage;

public class FXApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        WelcomeUI.show(primaryStage);
    }
}
