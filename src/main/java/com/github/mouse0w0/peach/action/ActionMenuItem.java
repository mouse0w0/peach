package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.data.DataManager;
import com.github.mouse0w0.peach.icon.Icon;
import javafx.scene.control.MenuItem;
import org.jetbrains.annotations.NotNull;

public final class ActionMenuItem extends MenuItem implements ActionHolder, Updatable {
    private final Action action;
    private final Presentation presentation;

    ActionMenuItem(Action action) {
        this.action = action;
        this.presentation = new Presentation(action, this::onPropertyChanged);

        setText(presentation.getText());
        Icon.setIcon(this, presentation.getIcon());

        setOnAction(this::perform);
    }

    private void onPropertyChanged(String propertyName, Object oldValue, Object newValue) {
        switch (propertyName) {
            case Presentation.TEXT_PROP -> setText((String) newValue);
            case Presentation.ICON_PROP -> Icon.setIcon(this, (Icon) newValue);
            case Presentation.DISABLE_PROP -> setDisable((boolean) newValue);
            case Presentation.VISIBLE_PROP -> setVisible((boolean) newValue);
        }
    }

    @Override
    public @NotNull Action getAction() {
        return action;
    }

    @Override
    public void update() {
        action.update(new ActionEvent(null, presentation, DataManager.getInstance().getDataContext(this)));
    }

    private void perform(javafx.event.ActionEvent event) {
        action.perform(new ActionEvent(event, presentation, DataManager.getInstance().getDataContext(this)));
    }
}
