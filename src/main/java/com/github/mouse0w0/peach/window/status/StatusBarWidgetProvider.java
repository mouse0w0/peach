package com.github.mouse0w0.peach.window.status;

import com.github.mouse0w0.peach.extension.ExtensionPointName;
import com.github.mouse0w0.peach.project.Project;

public interface StatusBarWidgetProvider {
    ExtensionPointName<StatusBarWidgetProvider> EXTENSION_POINT = ExtensionPointName.of("peach.statusBarWidgetProvider");

    String getId();

    String getDisplayName();

    StatusBarWidget createWidget(Project project);

    default StatusBarPosition getPosition() {
        return StatusBarPosition.RIGHT;
    }

    default boolean isAvailable(Project project) {
        return true;
    }

    default boolean isConfigurable() {
        return true;
    }

    default boolean isEnabledByDefault() {
        return true;
    }
}
