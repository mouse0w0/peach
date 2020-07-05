package com.github.mouse0w0.peach.ui.wizard;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.List;

public abstract class Wizard {

    private final List<WizardStep> wizardSteps = new ArrayList<>();

    private int currentStep = 0;

    private BorderPane content;

    @FXML
    private Button previous;
    @FXML
    private Button next;
    @FXML
    private Button cancel;

    private boolean finished;

    public Wizard() {
        content = FXUtils.loadFXML(null, this, "ui/wizard/Wizard.fxml");
    }

    public Parent getContent() {
        return content;
    }

    public void onFinished() {
    }

    public void onCancelled() {

    }

    public void initialize() {
        WizardStep step = getCurrentStep();
        step.initialize();
        content.setCenter(step.getNode());
        updateButtons();
    }

    public void addWizardStep(WizardStep step) {
        wizardSteps.add(step);
    }

    public WizardStep getCurrentStep() {
        return wizardSteps.get(currentStep);
    }

    public boolean isFirstStep() {
        return currentStep == 0;
    }

    public boolean isLastStep() {
        return currentStep == wizardSteps.size() - 1;
    }

    public Button getPreviousButton() {
        return previous;
    }

    public Button getNextButton() {
        return next;
    }

    public Button getCancelButton() {
        return cancel;
    }

    protected void updateButtons() {
        getPreviousButton().setDisable(isFirstStep());

        getNextButton().setText(isLastStep() ? I18n.translate("ui.wizard.finish") : I18n.translate("ui.wizard.next"));
    }

    public boolean isFinished() {
        return finished;
    }

    @FXML
    private void onPrevious() {
        if (isFirstStep()) return;

        currentStep--;
        WizardStep step = getCurrentStep();
        content.setCenter(step.getNode());
        updateButtons();
    }

    @FXML
    private void onNext() {
        WizardStep step = getCurrentStep();
        if (!step.validate()) return;

        step.updateDataModel();

        if (isLastStep()) {
            finished = true;
            onFinished();
        } else {
            currentStep++;
            step = getCurrentStep();
            step.initialize();
            content.setCenter(step.getNode());
            updateButtons();
        }
    }

    @FXML
    private void onCancel() {
        finished = false;
        onCancelled();
    }

    public void dispose() {
        wizardSteps.forEach(WizardStep::dispose);
    }
}
