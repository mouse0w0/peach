package com.github.mouse0w0.peach.ui.wizard;

import javafx.scene.Node;

public interface WizardStep {

    default String getName() {
        return getClass().getName();
    }

    Node getContent();

    void initialize();

    boolean validate();

    void updateDataModel();

    void dispose();
}
