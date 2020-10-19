package com.github.mouse0w0.peach.component;

import com.github.mouse0w0.peach.util.Disposable;

public interface ComponentManager extends Disposable {

    default <T> T getService(Class<T> classOfT) {
        return getService(classOfT, true);
    }

    default <T> T getServiceIfCreated(Class<T> classOfT) {
        return getService(classOfT, false);
    }

    <T> T getService(Class<T> classOfT, boolean createIfNeeded);
}
