package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.data.DataContext;
import com.github.mouse0w0.peach.data.DataKey;
import com.github.mouse0w0.peach.util.Validate;
import javafx.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ActionEvent {
    private final Event event;
    private final Presentation presentation;
    private final DataContext dataContext;

    public ActionEvent(@Nullable Event event, @NotNull Presentation presentation, @NotNull DataContext dataContext) {
        this.event = event;
        this.presentation = Validate.notNull(presentation);
        this.dataContext = Validate.notNull(dataContext);
    }

    @Nullable
    public Event getEvent() {
        return event;
    }

    @NotNull
    public Presentation getPresentation() {
        return presentation;
    }

    @NotNull
    public DataContext getDataContext() {
        return dataContext;
    }

    public Object getData(@NotNull String key) {
        return dataContext.getData(key);
    }

    public <T> T getData(@NotNull DataKey<T> key) {
        return dataContext.getData(key);
    }
}
