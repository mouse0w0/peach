package com.github.mouse0w0.peach.service.store;

import com.github.mouse0w0.peach.service.PersistentService;

public interface ServiceStore {
    void loadService(PersistentService service);

    void saveService(PersistentService service);
}
