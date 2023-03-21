package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.data.DataContext;
import com.github.mouse0w0.peach.data.DataKey;
import com.github.mouse0w0.peach.data.DataManager;
import com.github.mouse0w0.peach.window.WindowManager;
import javafx.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ActionEvent implements DataContext {

    private final Event event;
    private final DataContext dataContext;

    public ActionEvent(@Nullable Event event) {
        this(event, DataManager.getInstance().getDataContext(WindowManager.getInstance().getFocusedNode()));
    }

    public ActionEvent(@Nullable Event event, @NotNull DataContext dataContext) {
        this.event = event;
        this.dataContext = dataContext;
    }

    @Nullable
    public Event getEvent() {
        return event;
    }

    @NotNull
    public DataContext getDataContext() {
        return dataContext;
    }

    @Override
    public Object getData(@NotNull String key) {
        return dataContext.getData(key);
    }

    @Override
    public <T> T getData(@NotNull DataKey<T> key) {
        return dataContext.getData(key);
    }
}
