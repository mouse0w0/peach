package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.data.DataManager;
import com.github.mouse0w0.peach.icon.Icon;
import com.github.mouse0w0.peach.util.property.PropertyObservable;
import javafx.event.Event;
import javafx.scene.control.MenuItem;
import org.jetbrains.annotations.NotNull;

public class ActionMenuItem extends MenuItem implements ActionHolder {
    private final Action action;
    private final Presentation presentation;

    ActionMenuItem(Action action) {
        this.action = action;
        this.presentation = new Presentation(action);
        this.presentation.addListener(this::onPropertyChanged);

        setText(presentation.getText());
        Utils.setIcon(graphicProperty(), presentation.getIcon());

        setOnAction(this::perform);
    }

    @Override
    public @NotNull Action getAction() {
        return action;
    }

    private void onPropertyChanged(PropertyObservable property, String propertyName, Object oldValue, Object newValue) {
        switch (propertyName) {
            case Presentation.TEXT_PROP -> setText((String) newValue);
            case Presentation.ICON_PROP -> Utils.setIcon(graphicProperty(), (Icon) newValue);
            case Presentation.DISABLE_PROP -> setDisable((boolean) newValue);
            case Presentation.VISIBLE_PROP -> setVisible((boolean) newValue);
        }
    }

    private void perform(javafx.event.ActionEvent event) {
        action.perform(new ActionEvent(event, presentation, DataManager.getInstance().getDataContext(this)));
    }

    void update(Event event) {
        action.update(new ActionEvent(event, presentation, DataManager.getInstance().getDataContext(this)));
    }
}
