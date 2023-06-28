package com.github.mouse0w0.peach.util.property;

import org.jetbrains.annotations.NotNull;

public interface ObservableObject {
    void addListener(@NotNull PropertyChangeListener listener);

    void removeListener(@NotNull PropertyChangeListener listener);
}
