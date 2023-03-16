package com.github.mouse0w0.peach.data;

import com.github.mouse0w0.peach.Peach;

import javax.annotation.Nonnull;

public interface DataManager {

    static DataManager getInstance() {
        return Peach.getInstance().getService(DataManager.class);
    }

    DataContext getDataContext(@Nonnull Object source);

    DataProvider getDataProvider(@Nonnull Object o);

    void registerDataProvider(@Nonnull Object o, @Nonnull DataProvider dataProvider);

    void unregisterDataProvider(@Nonnull Object o);
}
