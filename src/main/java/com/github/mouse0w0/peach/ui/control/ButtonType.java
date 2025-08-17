package com.github.mouse0w0.peach.ui.control;

import com.github.mouse0w0.peach.l10n.AppL10n;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import org.jetbrains.annotations.ApiStatus;

public class ButtonType {
    public static final ButtonType APPLY = localized("button.apply", ButtonBar.ButtonData.APPLY);

    public static final ButtonType OK = localized("button.ok", ButtonBar.ButtonData.OK_DONE);

    public static final ButtonType CANCEL = localized("button.cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

    public static final ButtonType CLOSE = localized("button.close", ButtonBar.ButtonData.CANCEL_CLOSE);

    public static final ButtonType YES = localized("button.yes", ButtonBar.ButtonData.YES);

    public static final ButtonType NO = localized("button.no", ButtonBar.ButtonData.NO);

    public static final ButtonType FINISH = localized("button.finish", ButtonBar.ButtonData.FINISH);

    public static final ButtonType NEXT = localized("button.next", ButtonBar.ButtonData.NEXT_FORWARD);

    public static final ButtonType PREVIOUS = localized("button.previous", ButtonBar.ButtonData.BACK_PREVIOUS);

    public static final String BUTTON_TYPE_PROPERTY = "ButtonType";

    private final String text;
    private final ButtonBar.ButtonData buttonData;

    @ApiStatus.Internal
    public static ButtonType localized(String translationKey, ButtonBar.ButtonData buttonData) {
        return new ButtonType(AppL10n.localize(translationKey), buttonData);
    }

    public static ButtonType getButtonType(Node button) {
        return button.hasProperties() ? (ButtonType) button.getProperties().get(BUTTON_TYPE_PROPERTY) : null;
    }

    public ButtonType(String text, ButtonBar.ButtonData buttonData) {
        this.text = text;
        this.buttonData = buttonData;
    }

    public ButtonBar.ButtonData getButtonData() {
        return buttonData;
    }

    public String getText() {
        return text;
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
