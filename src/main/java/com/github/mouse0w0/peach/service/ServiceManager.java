package com.github.mouse0w0.peach.service;

import com.github.mouse0w0.peach.data.DataHolder;

public interface ServiceManager extends DataHolder {

    default <T> T getService(Class<T> classOfT) {
        return getService(classOfT, true);
    }

    default <T> T getServiceIfCreated(Class<T> classOfT) {
        return getService(classOfT, false);
    }

    <T> T getService(Class<T> classOfT, boolean createIfNeeded);
}
