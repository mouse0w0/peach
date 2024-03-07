package com.github.mouse0w0.peach.window.status;

import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.action.ToggleAction;
import com.github.mouse0w0.peach.window.ProjectWindow;
import com.github.mouse0w0.peach.window.WindowManager;
import org.jetbrains.annotations.NotNull;

public final class StatusBarWidgetToggleAction extends ToggleAction {
    private final StatusBarWidgetProvider provider;

    public StatusBarWidgetToggleAction(StatusBarWidgetProvider provider) {
        this.provider = provider;
        setText(provider.getDisplayName());
    }

    @Override
    public boolean isSelected(@NotNull ActionEvent event) {
        return StatusBarWidgetManager.getInstance().isEnabled(provider);
    }

    @Override
    public void setSelected(@NotNull ActionEvent event, boolean selected) {
        StatusBarWidgetManager.getInstance().setEnabled(provider, selected);
        if (selected) {
            for (ProjectWindow window : WindowManager.getInstance().getWindows()) {
                window.getStatusBar().addWidget(provider.getId());
            }
        } else {
            for (ProjectWindow window : WindowManager.getInstance().getWindows()) {
                window.getStatusBar().removeWidget(provider.getId());
            }
        }
    }
}
