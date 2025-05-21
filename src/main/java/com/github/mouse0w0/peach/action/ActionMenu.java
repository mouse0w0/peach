package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.data.DataManager;
import com.github.mouse0w0.peach.icon.Icon;
import javafx.event.Event;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import org.jetbrains.annotations.NotNull;

public final class ActionMenu extends Menu implements ActionHolder, Presentation, Updatable {
    private final ActionGroup group;

    private final MenuItem placeholder = new MenuItem();

    ActionMenu(ActionGroup group) {
        this.group = group;

        setText(group.getText());
        setIcon(group.getIcon());

        addEventFilter(ON_SHOWING, this::updateChildren);

        // Fix JavaFX don't show empty menu.
        getItems().add(placeholder);
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
    public @NotNull ActionGroup getAction() {
        return group;
    }

    @Override
    public void show() {
        fillMenu();
        super.show();
        if (getItems().isEmpty()) {
            getItems().add(placeholder);
        }
    }

    @Override
    public void update() {
        group.update(new ActionEvent(null, this, DataManager.getInstance().getDataContext(this)));
    }

    private void fillMenu() {
        getItems().clear();
        Utils.fillMenu(group, new ActionEvent(null, this, DataManager.getInstance().getDataContext(this)), getItems());
    }

    private void updateChildren(Event event) {
        Utils.update(getItems());
        Utils.updateSeparatorVisibility(getItems());
    }
}
