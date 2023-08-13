package com.github.mouse0w0.peach.ui.util;

import javafx.beans.value.WritableValue;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class WritableValueWrapper<T> implements WritableValue<T> {
    private final Supplier<T> getter;
    private final Consumer<T> setter;

    public WritableValueWrapper(Supplier<T> getter, Consumer<T> setter) {
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public T getValue() {
        return getter.get();
    }

    @Override
    public void setValue(T value) {
        setter.accept(value);
    }
}
