package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.data.DataManager;
import com.github.mouse0w0.peach.icon.Icon;
import com.github.mouse0w0.peach.util.property.ObservableObject;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import org.jetbrains.annotations.NotNull;

public class ActionMenu extends Menu implements ActionHolder {
    private final ActionGroup group;
    private final Presentation presentation;

    private boolean initialized;

    ActionMenu(ActionGroup group) {
        this.group = group;
        this.presentation = new Presentation(group);
        this.presentation.addListener(this::onPropertyChanged);

        setText(presentation.getText());
        Utils.setIcon(graphicProperty(), presentation.getIcon());

        setOnShowing(this::updateChildren);

        // Fix JavaFX don't show empty menu.
        if (group.getChildren().isEmpty()) {
            initialized = true;
        } else {
            getItems().add(new MenuItem());
        }
    }

    @Override
    public @NotNull ActionGroup getAction() {
        return group;
    }

    @Override
    public void show() {
        initialize();
        super.show();
    }

    private void initialize() {
        if (initialized) return;
        initialized = true;

        ObservableList<MenuItem> items = getItems();
        items.clear();
        Utils.fillMenu(group, items);
    }

    void update(Event event) {
        group.update(new ActionEvent(event, presentation, DataManager.getInstance().getDataContext(this)));
    }

    private void updateChildren(Event event) {
        Utils.update(event, getItems());
        Utils.updateSeparatorVisibility(getItems());
    }

    private void onPropertyChanged(ObservableObject property, String propertyName, Object oldValue, Object newValue) {
        switch (propertyName) {
            case Presentation.TEXT_PROP -> setText((String) newValue);
            case Presentation.ICON_PROP -> Utils.setIcon(graphicProperty(), (Icon) newValue);
            case Presentation.DISABLE_PROP -> setDisable((boolean) newValue);
            case Presentation.VISIBLE_PROP -> setVisible((boolean) newValue);
        }
    }

}
