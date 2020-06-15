package com.github.mouse0w0.peach.ui;

import com.github.mouse0w0.peach.ui.util.FXUtils;
import javafx.scene.layout.BorderPane;

public class MainUI extends BorderPane {

    public MainUI() {
        FXUtils.loadFXML(this, "ui/MainUI.fxml");
    }
}
