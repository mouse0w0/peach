package com.github.mouse0w0.peach.javafx;

import com.sun.javafx.binding.ExpressionHelper;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.SelectionModel;

import java.lang.ref.WeakReference;

public class SelectedItemProperty<T, SM extends SelectionModel<T>> extends ObjectProperty<T> {
    private final SM selectionModel;
    private final ReadOnlyObjectProperty<T> selectedItemProperty;
    private final InvalidationListener selectedItemListener;

    private ObservableValue<? extends T> observable;
    private InvalidationListener listener;
    private ExpressionHelper<T> helper;

    public SelectedItemProperty(SM selectionModel) {
        this.selectionModel = selectionModel;
        this.selectedItemProperty = selectionModel.selectedItemProperty();
        this.selectedItemListener = new SelectedItemListener(this);
        this.selectedItemProperty.addListener(this.selectedItemListener);
    }

    @Override
    public Object getBean() {
        return selectedItemProperty.getBean();
    }

    @Override
    public String getName() {
        return selectedItemProperty.getName();
    }

    @Override
    public T get() {
        return selectedItemProperty.get();
    }

    @Override
    public void set(T newValue) {
        selectionModel.select(newValue);
    }

    @Override
    public void addListener(InvalidationListener listener) {
        helper = ExpressionHelper.addListener(helper, this, listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        helper = ExpressionHelper.removeListener(helper, listener);
    }

    @Override
    public void addListener(ChangeListener<? super T> listener) {
        helper = ExpressionHelper.addListener(helper, this, listener);
    }

    @Override
    public void removeListener(ChangeListener<? super T> listener) {
        helper = ExpressionHelper.removeListener(helper, listener);
    }

    protected void fireValueChangedEvent() {
        ExpressionHelper.fireValueChangedEvent(helper);
    }

    private void markInvalid() {
        set(observable.getValue());
    }

    @Override
    public void bind(ObservableValue<? extends T> newObservable) {
        if (newObservable == null) {
            throw new NullPointerException("Cannot bind to null");
        }

        if (!newObservable.equals(observable)) {
            unbind();
            observable = newObservable;
            if (listener == null) {
                listener = new Listener(this);
            }
            observable.addListener(listener);
            markInvalid();
        }
    }

    @Override
    public void unbind() {
        if (observable != null) {
            observable.removeListener(listener);
            observable = null;
        }
    }

    @Override
    public boolean isBound() {
        return observable != null;
    }

    private static class SelectedItemListener implements InvalidationListener {

        private final WeakReference<SelectedItemProperty<?, ?>> wref;

        public SelectedItemListener(SelectedItemProperty<?, ?> ref) {
            this.wref = new WeakReference<>(ref);
        }

        @Override
        public void invalidated(Observable observable) {
            SelectedItemProperty<?, ?> ref = wref.get();
            if (ref == null) {
                observable.removeListener(this);
            } else {
                ref.fireValueChangedEvent();
            }
        }
    }

    private static class Listener implements InvalidationListener {

        private final WeakReference<SelectedItemProperty<?, ?>> wref;

        public Listener(SelectedItemProperty<?, ?> ref) {
            this.wref = new WeakReference<>(ref);
        }

        @Override
        public void invalidated(Observable observable) {
            SelectedItemProperty<?, ?> ref = wref.get();
            if (ref == null) {
                observable.removeListener(this);
            } else {
                ref.markInvalid();
            }
        }
    }
}
