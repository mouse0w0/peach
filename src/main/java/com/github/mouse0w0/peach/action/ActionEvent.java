package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.data.DataContext;
import com.github.mouse0w0.peach.data.DataKey;
import com.github.mouse0w0.peach.data.DataManager;

import javax.annotation.Nonnull;

public class ActionEvent implements DataContext {

    private final Object source;

    private final DataContext dataContext;

    public ActionEvent(@Nonnull Object source) {
        if (source == null) throw new NullPointerException("source");
        this.source = source;
        this.dataContext = DataManager.getInstance().getDataContext(source);
    }

    public Object getSource() {
        return source;
    }

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
