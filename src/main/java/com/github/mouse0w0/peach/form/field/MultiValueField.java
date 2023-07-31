package com.github.mouse0w0.peach.form.field;

import javafx.collections.ObservableList;

import java.util.Collection;

public abstract class MultiValueField<T> extends Field {
    public abstract ObservableList<T> getValues();

    public abstract void setValues(Collection<? extends T> collection);

    public abstract void setValues(T... elements);
}
