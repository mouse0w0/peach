package com.github.mouse0w0.peach.windowState;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.project.Project;
import javafx.stage.Window;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface WindowStateService {
    static WindowStateService getInstance() {
        return Peach.getInstance().getService(WindowStateService.class);
    }

    static WindowStateService getInstance(@Nullable Project project) {
        return project != null ? project.getService(WindowStateService.class) : Peach.getInstance().getService(WindowStateService.class);
    }

    void register(@NotNull Window window, @NotNull String stateId);
}
