package com.github.mouse0w0.peach.wizard;

import com.github.mouse0w0.i18n.I18n;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public abstract class Wizard {

    private final List<WizardStep> wizardSteps = new ArrayList<>();
    private int currentStep = -1;

    private BorderPane content;

    private Button previous;
    private Button next;
    private Button cancel;

    private boolean cancelled;

    private final List<Runnable> closedCallbacks = new ArrayList<>();

    public Wizard() {
        content = new BorderPane();
        content.setPadding(new Insets(8));
        content.getStyleClass().add("wizard");

        previous = new Button(I18n.translate("wizard.previous"));
        previous.getStyleClass().add("previous");
        previous.setOnAction(event -> onPrevious());
        next = new Button(I18n.translate("wizard.next"));
        next.setDefaultButton(true);
        next.setOnAction(event -> onNext());
        cancel = new Button(I18n.translate("wizard.cancel"));
        cancel.setCancelButton(true);
        cancel.setOnAction(event -> onCancel());

        HBox buttonBar = new HBox(8, previous, next, cancel);
        buttonBar.getStyleClass().add("button-bar");
        buttonBar.setAlignment(Pos.CENTER_RIGHT);

        content.setBottom(buttonBar);
    }

    public String getName() {
        return getClass().getName();
    }

    protected void onFinishWizard() {
        // empty
    }

    protected void onCancelWizard() {
        // empty
    }

    private void fireClosedCallbacks() {
        closedCallbacks.forEach(Runnable::run);
    }

    public void addClosedCallback(Runnable runnable) {
        closedCallbacks.add(runnable);
    }

    public void removeClosedCallback(Runnable runnable) {
        closedCallbacks.remove(runnable);
    }

    public Parent getContent() {
        return content;
    }

    protected void init() {
        currentStep = 0;
        WizardStep step = getCurrentStep();
        step.init();
        content.setCenter(step.getContent());
    }

    public void addWizardStep(WizardStep step) {
        wizardSteps.add(step);
        if (currentStep == -1) {
            init();
        }
        updateButtons();
    }

    public WizardStep getCurrentStep() {
        return wizardSteps.get(currentStep);
    }

    public int getStepCount() {
        return wizardSteps.size();
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
        getPreviousButton().setVisible(getStepCount() != 1);
        getPreviousButton().setDisable(isFirstStep());

        getNextButton().setText(isLastStep() ? I18n.translate("wizard.finish") : I18n.translate("wizard.next"));
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        if (cancelled) return;
        cancelled = true;
        onCancelWizard();
        fireClosedCallbacks();
        dispose();
    }

    private void onPrevious() {
        if (isFirstStep()) return;

        currentStep--;
        WizardStep step = getCurrentStep();
        content.setCenter(step.getContent());
        updateButtons();
    }

    private void onNext() {
        WizardStep step = getCurrentStep();
        if (!step.validate()) return;

        step.updateDataModel();

        if (isLastStep()) {
            onFinishWizard();
            fireClosedCallbacks();
            dispose();
        } else {
            currentStep++;
            step = getCurrentStep();
            step.init();
            content.setCenter(step.getContent());
            updateButtons();
        }
    }

    private void onCancel() {
        cancel();
    }

    public void dispose() {
        wizardSteps.forEach(WizardStep::dispose);
    }
}
