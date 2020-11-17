package com.github.mouse0w0.peach.wizard;

import javafx.scene.Node;

public abstract class WizardStepBase implements WizardStep {

    private Node content;

    @Override
    public Node getContent() {
        return content;
    }

    protected void setContent(Node content) {
        this.content = content;
    }
}
