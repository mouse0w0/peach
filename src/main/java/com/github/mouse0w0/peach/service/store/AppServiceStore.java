package com.github.mouse0w0.peach.service.store;

import com.github.mouse0w0.peach.Peach;

public class AppServiceStore extends ServiceStoreBase {
    public AppServiceStore() {
        super(Peach.getInstance().getUserPropertiesPath());
    }
}
