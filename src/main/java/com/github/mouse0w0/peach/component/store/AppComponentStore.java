package com.github.mouse0w0.peach.component.store;

import com.github.mouse0w0.peach.Peach;

public class AppComponentStore extends ComponentStoreBase {
    public AppComponentStore() {
        super(Peach.getInstance().getUserPropertiesPath());
    }
}
