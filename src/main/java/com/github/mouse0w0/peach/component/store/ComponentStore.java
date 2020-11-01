package com.github.mouse0w0.peach.component.store;

import com.github.mouse0w0.peach.component.PersistentComponent;

public interface ComponentStore {

    void loadComponent(PersistentComponent component);

    void saveComponent(PersistentComponent component);
}
