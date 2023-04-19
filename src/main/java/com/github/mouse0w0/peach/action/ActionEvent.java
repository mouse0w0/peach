package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.data.DataContext;
import com.github.mouse0w0.peach.data.DataKey;
import com.github.mouse0w0.peach.data.DataManager;
import com.github.mouse0w0.peach.util.Validate;
import org.jetbrains.annotations.NotNull;

public class ActionEvent implements DataContext {
    private final Object source;
    private final DataContext dataContext;

    public ActionEvent(@NotNull Object source) {
        this.source = Validate.notNull(source);
        this.dataContext = DataManager.getInstance().getDataContext(source);
    }

    public ActionEvent(@NotNull Object source, @NotNull DataContext dataContext) {
        this.source = Validate.notNull(source);
        this.dataContext = Validate.notNull(dataContext);
    }

    @NotNull
    public Object getSource() {
        return source;
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
