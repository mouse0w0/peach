package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.data.DataContext;
import com.github.mouse0w0.peach.data.DataKey;
import com.github.mouse0w0.peach.data.DataManager;
import com.github.mouse0w0.peach.window.WindowManager;
import javafx.event.Event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ActionEvent implements DataContext {

    private final Event event;
    private final DataContext dataContext;

    public ActionEvent(@Nullable Event event) {
        this(event, DataManager.getInstance().getDataContext(WindowManager.getInstance().getFocusedNode()));
    }

    public ActionEvent(@Nullable Event event, @Nonnull DataContext dataContext) {
        this.event = event;
        this.dataContext = dataContext;
    }

    @Nullable
    public Event getEvent() {
        return event;
    }

    @Nonnull
    public DataContext getDataContext() {
        return dataContext;
    }

    @Override
    public Object getData(@Nonnull String key) {
        return dataContext.getData(key);
    }

    @Override
    public <T> T getData(@Nonnull DataKey<T> key) {
        return dataContext.getData(key);
    }
}
