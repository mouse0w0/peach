package com.github.mouse0w0.peach.service;

import com.github.mouse0w0.peach.dispose.Disposable;
import com.github.mouse0w0.peach.message.MessageBus;

public interface ServiceManager extends Disposable {

    <T> T getService(Class<T> classOfT);

    <T> T getServiceIfCreated(Class<T> classOfT);

    MessageBus getMessageBus();
}
