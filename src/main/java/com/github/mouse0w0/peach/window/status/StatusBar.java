package com.github.mouse0w0.peach.window.status;

import com.github.mouse0w0.peach.project.Project;
import org.jetbrains.annotations.ApiStatus;

public interface StatusBar {
    Project getProject();

    StatusBarWidget getWidget(String id);

    boolean hasWidget(String id);

    @ApiStatus.Internal
    void addWidget(String id);

    @ApiStatus.Internal
    void removeWidget(String id);
}
