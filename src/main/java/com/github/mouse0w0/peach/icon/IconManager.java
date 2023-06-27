package com.github.mouse0w0.peach.icon;

import com.github.mouse0w0.peach.Peach;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public interface IconManager {
    static IconManager getInstance() {
        return Peach.getInstance().getService(IconManager.class);
    }

    Icon getIcon(@NotNull String name);
}
