package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.icon.IconManager;
import com.github.mouse0w0.peach.util.StringUtils;
import com.github.mouse0w0.peach.util.property.PropertyChangeListener;
import com.github.mouse0w0.peach.util.property.PropertyObservable;
import com.github.mouse0w0.peach.util.property.WeakPropertyChangeListener;
import javafx.event.Event;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import org.jetbrains.annotations.NotNull;

public class ActionMenu extends Menu implements ActionHolder {
    private final ActionGroup group;
    private final PropertyChangeListener listener;

    private boolean initialized;

    ActionMenu(ActionGroup group) {
        this.group = group;

        setOnShowing(this::update);

        setText(group.getText());
        setIcon(group.getIcon());
        setDisable(group.isDisable());
        setVisible(group.isVisible());

        this.listener = this::onPropertyChanged;
        group.addListener(new WeakPropertyChangeListener(listener));

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
        if (!initialized) {
            initialized = true;
            Utils.fillMenu(group, getItems());
        }
    }

    private void update(Event event) {
        Utils.update(group, event);
        Utils.updateSeparatorVisibility(getItems());
    }

    private void onPropertyChanged(PropertyObservable property, String propertyName, Object oldValue, Object newValue) {
        switch (propertyName) {
            case Action.TEXT_PROP -> setText((String) newValue);
            case Action.ICON_PROP -> setIcon((String) newValue);
            case Action.DISABLE_PROP -> setDisable((boolean) newValue);
            case Action.VISIBLE_PROP -> setVisible((boolean) newValue);
        }
    }

    private void setIcon(String value) {
        if (StringUtils.isEmpty(value)) {
            setGraphic(null);
        } else {
            setGraphic(IconManager.getInstance().createNode(value));
        }
    }
}
