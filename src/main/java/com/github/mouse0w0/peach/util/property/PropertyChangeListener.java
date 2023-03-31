package com.github.mouse0w0.peach.util.property;

public interface PropertyChangeListener {
    void propertyChanged(PropertyObservable observable, String propertyName, Object oldValue, Object newValue);
}
