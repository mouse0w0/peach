package com.github.mouse0w0.peach.service.store;

import com.github.mouse0w0.peach.service.PersistentService;

import java.nio.file.Path;

public interface ServiceStore {

    Path getStorePath();

    void loadService(PersistentService service);

    void saveService(PersistentService service);
}
