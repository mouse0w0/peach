package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.data.DataContext;
import com.github.mouse0w0.peach.data.DataKey;
import javafx.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class ActionEvent {
    private final Event event;
    private final Presentation presentation;
    private final DataContext dataContext;

    public ActionEvent(@Nullable Event event, @NotNull Presentation presentation, @NotNull DataContext dataContext) {
        this.event = event;
        this.presentation = Objects.requireNonNull(presentation);
        this.dataContext = Objects.requireNonNull(dataContext);
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
