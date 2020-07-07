package com.github.mouse0w0.peach.ui.wizard;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.List;

public abstract class Wizard {

    private final List<WizardStep> wizardSteps = new ArrayList<>();
    private int currentStep = -1;

    private BorderPane content;

    @FXML
    private Button previous;
    @FXML
    private Button next;
    @FXML
    private Button cancel;

    private boolean ok;

    private final List<Runnable> closedCallbacks = new ArrayList<>();

    public static Tab createTab(Wizard wizard) {
        Tab tab = new Tab();
        tab.setContent(wizard.getContent());
        tab.setText(wizard.getName());
        tab.setClosable(true);
        tab.setOnCloseRequest(event -> {
            event.consume();
            wizard.cancel();
        });
        wizard.addClosedCallback(() -> {
            if (tab.getTabPane() != null) {
                tab.getTabPane().getTabs().remove(tab);
            }
        });
        return tab;
    }

    public Wizard() {
        content = FXUtils.loadFXML(null, this, "ui/wizard/Wizard.fxml");
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
        updateButtons();
    }

    public void addWizardStep(WizardStep step) {
        wizardSteps.add(step);
        if (currentStep == -1) {
            init();
        }
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

    public boolean isOk() {
        return ok;
    }

    public void cancel() {
        ok = false;
        onCancelWizard();
        fireClosedCallbacks();
        dispose();
    }

    @FXML
    private void onPrevious() {
        if (isFirstStep()) return;

        currentStep--;
        WizardStep step = getCurrentStep();
        content.setCenter(step.getContent());
        updateButtons();
    }

    @FXML
    private void onNext() {
        WizardStep step = getCurrentStep();
        if (!step.validate()) return;

        step.updateDataModel();

        if (isLastStep()) {
            ok = true;
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

    @FXML
    private void onCancel() {
        cancel();
    }

    public void dispose() {
        wizardSteps.forEach(WizardStep::dispose);
    }
}
