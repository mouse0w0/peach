package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.data.DataManager;
import com.github.mouse0w0.peach.icon.Icon;
import javafx.scene.control.MenuItem;
import org.jetbrains.annotations.NotNull;

public final class ActionMenuItem extends MenuItem implements ActionHolder, Presentation, Updatable {
    private final Action action;

    ActionMenuItem(Action action) {
        this.action = action;

        setText(action.getText());
        setIcon(action.getIcon());

        setOnAction(this::perform);
    }

    private String description;

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String value) {
        description = value;
    }

    private Icon icon;

    @Override
    public Icon getIcon() {
        return icon;
    }

    @Override
    public void setIcon(Icon value) {
        icon = value;
        Icon.setIcon(this, value);
    }

    @Override
    public @NotNull Action getAction() {
        return action;
    }

    @Override
    public void update() {
        action.update(new ActionEvent(null, this, DataManager.getInstance().getDataContext(this)));
    }

    private void perform(javafx.event.ActionEvent event) {
        action.perform(new ActionEvent(event, this, DataManager.getInstance().getDataContext(this)));
    }
}
