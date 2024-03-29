package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.data.DataManager;
import com.github.mouse0w0.peach.icon.Icon;
import javafx.scene.control.Button;
import org.jetbrains.annotations.NotNull;

public final class ActionButton extends Button implements ActionHolder {
    private final Action action;
    private final Presentation presentation;

    ActionButton(Action action) {
        this.action = action;
        this.presentation = new Presentation(action, this::onPropertyChanged);

        setText(action.getText());
        Icon.apply(graphicProperty(), action.getIcon());

        skinProperty().addListener(observable -> update());
        setOnAction(this::perform);
    }

    private void onPropertyChanged(String propertyName, Object oldValue, Object newValue) {
        switch (propertyName) {
            case Presentation.TEXT_PROP -> setText((String) newValue);
            case Presentation.ICON_PROP -> Icon.apply(graphicProperty(), (Icon) newValue);
            case Presentation.DISABLE_PROP -> setDisable((boolean) newValue);
            case Presentation.VISIBLE_PROP -> setVisible((boolean) newValue);
        }
    }

    @Override
    public @NotNull Action getAction() {
        return action;
    }

    private void perform(javafx.event.ActionEvent event) {
        action.perform(new ActionEvent(event, presentation, DataManager.getInstance().getDataContext(this)));
    }

    private void update() {
        action.update(new ActionEvent(null, presentation, DataManager.getInstance().getDataContext(this)));
    }
}
