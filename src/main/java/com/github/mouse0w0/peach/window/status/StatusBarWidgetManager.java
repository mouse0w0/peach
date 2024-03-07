package com.github.mouse0w0.peach.window.status;

import com.github.mouse0w0.peach.Peach;

import java.util.List;

public interface StatusBarWidgetManager {
    static StatusBarWidgetManager getInstance() {
        return Peach.getInstance().getService(StatusBarWidgetManager.class);
    }

    List<StatusBarWidgetProvider> getProviders();

    StatusBarWidgetProvider getProvider(String widgetId);

    int getIndex(String widgetId);

    boolean isEnabled(StatusBarWidgetProvider provider);

    void setEnabled(StatusBarWidgetProvider provider, boolean enabled);
}
