package com.github.mouse0w0.peach.javafx.control;

import com.github.mouse0w0.peach.l10n.AppL10n;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;

public class ButtonType {
    public static final ButtonType APPLY = new ButtonType(
            "button.apply", ButtonBar.ButtonData.APPLY);

    public static final ButtonType OK = new ButtonType(
            "button.ok", ButtonBar.ButtonData.OK_DONE);

    public static final ButtonType CANCEL = new ButtonType(
            "button.cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

    public static final ButtonType CLOSE = new ButtonType(
            "button.close", ButtonBar.ButtonData.CANCEL_CLOSE);

    public static final ButtonType YES = new ButtonType(
            "button.yes", ButtonBar.ButtonData.YES);

    public static final ButtonType NO = new ButtonType(
            "button.no", ButtonBar.ButtonData.NO);

    public static final ButtonType FINISH = new ButtonType(
            "button.finish", ButtonBar.ButtonData.FINISH);

    public static final ButtonType NEXT = new ButtonType(
            "button.next", ButtonBar.ButtonData.NEXT_FORWARD);

    public static final ButtonType PREVIOUS = new ButtonType(
            "button.previous", ButtonBar.ButtonData.BACK_PREVIOUS);

    public static final String BUTTON_TYPE_PROPERTY = "ButtonType";

    private final String translationKey;
    private final ButtonBar.ButtonData buttonData;

    public static ButtonType getButtonType(Node button) {
        return button.hasProperties() ? (ButtonType) button.getProperties().get(BUTTON_TYPE_PROPERTY) : null;
    }

    public ButtonType(String translationKey, ButtonBar.ButtonData buttonData) {
        this.translationKey = translationKey;
        this.buttonData = buttonData;
    }

    public ButtonBar.ButtonData getButtonData() {
        return buttonData;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public String getText() {
        return AppL10n.localize(translationKey);
    }

    public Button createButton() {
        Button button = new Button(getText());
        ButtonBar.ButtonData buttonData = getButtonData();
        ButtonBar.setButtonData(button, buttonData);
        button.setDefaultButton(buttonData.isDefaultButton());
        button.setCancelButton(buttonData.isCancelButton());
        button.getProperties().put(BUTTON_TYPE_PROPERTY, this);
        return button;
    }

    @Override
    public String toString() {
        return "ButtonType [text=" + getText() + ", buttonData=" + getButtonData() + "]";
    }
}
