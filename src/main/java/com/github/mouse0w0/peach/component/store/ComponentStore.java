package com.github.mouse0w0.peach.component.store;

import com.github.mouse0w0.peach.component.PersistentStateComponent;

import java.nio.file.Path;

public interface ComponentStore {

    Path getStorePath();

    void loadComponent(PersistentStateComponent component);

    void saveComponent(PersistentStateComponent component);
}
