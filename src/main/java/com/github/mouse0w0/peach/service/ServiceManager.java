package com.github.mouse0w0.peach.service;

public interface ServiceManager {

    default <T> T getService(Class<T> classOfT) {
        return getService(classOfT, true);
    }

    default <T> T getServiceIfCreated(Class<T> classOfT) {
        return getService(classOfT, false);
    }

    <T> T getService(Class<T> classOfT, boolean createIfNeeded);
}
