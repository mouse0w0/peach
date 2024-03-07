package com.github.mouse0w0.peach.window.status;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.action.ActionGroup;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class StatusBarWidgetGroup extends ActionGroup {
    @Override
    public List<Action> getChildren(@Nullable ActionEvent event) {
        List<Action> actions = new ArrayList<>();
        for (StatusBarWidgetProvider provider : StatusBarWidgetManager.getInstance().getProviders()) {
            if (provider.isConfigurable()) {
                actions.add(new StatusBarWidgetToggleAction(provider));
            }
        }
        return actions;
    }
}
