package com.github.mouse0w0.peach.util.property;

import java.util.Arrays;
import java.util.Objects;

public class PropertyListenerHelper {
    private final PropertyObservable observable;

    private PropertyChangeListener[] changeListeners;
    private int changeSize;
    private boolean locked;

    public static PropertyListenerHelper addListener(PropertyListenerHelper helper, PropertyObservable observable, PropertyChangeListener listener) {
        if (observable == null) throw new NullPointerException("observable");
        if (listener == null) throw new NullPointerException("listener");
        return helper != null ? helper.addListener(listener) : new PropertyListenerHelper(observable, listener);
    }

    public static PropertyListenerHelper removeListener(PropertyListenerHelper helper, PropertyChangeListener listener) {
        if (listener == null) throw new NullPointerException("listener");
        return helper != null ? helper.removeListener(listener) : null;
    }

    public static void firePropertyChange(PropertyListenerHelper helper, String propertyName, Object oldValue, Object newValue) {
        if (helper != null && !Objects.equals(oldValue, newValue)) {
            helper.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    public static void firePropertyChange(PropertyListenerHelper helper, String propertyName, boolean oldValue, boolean newValue) {
        if (helper != null && oldValue != newValue) {
            helper.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    public static void firePropertyChange(PropertyListenerHelper helper, String propertyName, int oldValue, int newValue) {
        if (helper != null && oldValue != newValue) {
            helper.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    public static void firePropertyChange(PropertyListenerHelper helper, String propertyName, long oldValue, long newValue) {
        if (helper != null && oldValue != newValue) {
            helper.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    public static void firePropertyChange(PropertyListenerHelper helper, String propertyName, float oldValue, float newValue) {
        if (helper != null && oldValue != newValue) {
            helper.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    public static void firePropertyChange(PropertyListenerHelper helper, String propertyName, double oldValue, double newValue) {
        if (helper != null && oldValue != newValue) {
            helper.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    private PropertyListenerHelper(PropertyObservable observable, PropertyChangeListener listener) {
        this.observable = observable;
        this.changeListeners = new PropertyChangeListener[]{listener};
        this.changeSize = 1;
    }

    private PropertyListenerHelper addListener(PropertyChangeListener listener) {
        final int oldCapacity = changeListeners.length;
        if (locked) {
            int newCapacity = changeSize < oldCapacity ? oldCapacity : oldCapacity * 3 / 2 + 1;
            changeListeners = Arrays.copyOf(changeListeners, newCapacity);
        } else if (changeSize == oldCapacity) {
            changeSize = trim(changeSize, changeListeners);
            if (changeSize == oldCapacity) {
                int newCapacity = oldCapacity * 3 / 2 + 1;
                changeListeners = Arrays.copyOf(changeListeners, newCapacity);
            }
        }
        changeListeners[changeSize++] = listener;
        return this;
    }

    private static int trim(int size, Object[] listeners) {
        int index = 0;
        for (; index < size; index++) {
            if (wasGarbageCollected(listeners[index])) {
                break;
            }
        }
        if (index < size) {
            for (int src = index + 1; src < size; src++) {
                if (!wasGarbageCollected(listeners[src])) {
                    listeners[index++] = listeners[src];
                }
            }
            int oldSize = size;
            size = index;
            for (; index < oldSize; index++) {
                listeners[index] = null;
            }
        }

        return size;
    }

    private static boolean wasGarbageCollected(Object listener) {
        return listener instanceof WeakListener && ((WeakListener) listener).wasGarbageCollected();
    }

    private PropertyListenerHelper removeListener(PropertyChangeListener listener) {
        for (int i = 0; i < changeSize; i++) {
            if (changeListeners[i].equals(listener)) {
                if (changeSize == 1) {
                    return null;
                } else {
                    final int numMoved = changeSize - i - 1;
                    final PropertyChangeListener[] oldListeners = changeListeners;
                    if (locked) {
                        changeListeners = new PropertyChangeListener[oldListeners.length];
                        System.arraycopy(oldListeners, 0, changeListeners, 0, i);
                    }
                    if (numMoved > 0) {
                        System.arraycopy(oldListeners, i + 1, changeListeners, i, numMoved);
                    }
                    changeSize--;
                    if (!locked) {
                        changeListeners[changeSize] = null;
                    }
                    break;
                }
            }
        }
        return this;
    }

    private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        final PropertyChangeListener[] changeListeners = this.changeListeners;
        final int changeSize = this.changeSize;
        try {
            locked = true;
            for (int i = 0; i < changeSize; i++) {
                try {
                    changeListeners[i].propertyChanged(observable, propertyName, oldValue, newValue);
                } catch (Exception e) {
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        } finally {
            locked = false;
        }
    }
}
