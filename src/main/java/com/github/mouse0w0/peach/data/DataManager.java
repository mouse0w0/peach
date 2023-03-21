package com.github.mouse0w0.peach.data;

import com.github.mouse0w0.peach.Peach;
import org.jetbrains.annotations.NotNull;

public interface DataManager {

    static DataManager getInstance() {
        return Peach.getInstance().getService(DataManager.class);
    }

    DataContext getDataContext(@NotNull Object source);

    DataProvider getDataProvider(@NotNull Object o);

    void registerDataProvider(@NotNull Object o, @NotNull DataProvider dataProvider);

    void unregisterDataProvider(@NotNull Object o);
}
