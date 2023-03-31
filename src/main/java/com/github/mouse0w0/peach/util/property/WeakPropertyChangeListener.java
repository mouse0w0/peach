package com.github.mouse0w0.peach.util.property;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public final class WeakPropertyChangeListener implements PropertyChangeListener, WeakListener {
    private final Reference<PropertyChangeListener> ref;

    public WeakPropertyChangeListener(PropertyChangeListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener");
        }
        this.ref = new WeakReference<>(listener);
    }

    public boolean wasGarbageCollected() {
        return ref.get() == null;
    }

    public PropertyChangeListener getListener() {
        return ref.get();
    }

    @Override
    public void propertyChanged(PropertyObservable observable, String propertyName, Object oldValue, Object newValue) {
        PropertyChangeListener listener = ref.get();
        if (listener != null) {
            listener.propertyChanged(observable, propertyName, oldValue, newValue);
        } else {
            observable.removeListener(this);
        }
    }
}
