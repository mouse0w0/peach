package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.data.DataManager;
import com.github.mouse0w0.peach.icon.Icon;
import javafx.scene.control.CheckMenuItem;
import org.jetbrains.annotations.NotNull;

public class ActionToggleMenuItem extends CheckMenuItem implements ActionHolder, Presentation, Updatable {
    private final ToggleAction action;

    ActionToggleMenuItem(ToggleAction action) {
        this.action = action;

        setText(action.getText());
        setIcon(action.getIcon());

        selectedProperty().addListener(observable -> {
            if (isSelected()) {
                Icon.setIcon(this, null);
            } else {
                Icon.setIcon(this, getIcon());
            }
        });
        setOnAction(this::perform);
    }

    private String description;

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    private Icon icon;

    @Override
    public Icon getIcon() {
        return icon;
    }

    @Override
    public void setIcon(Icon icon) {
        this.icon = icon;
        Icon.setIcon(this, icon);
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
