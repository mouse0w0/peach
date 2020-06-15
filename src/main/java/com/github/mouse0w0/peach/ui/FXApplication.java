package com.github.mouse0w0.peach.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(new MainUI()));
        primaryStage.setTitle("Peach");
        primaryStage.show();
    }
}
