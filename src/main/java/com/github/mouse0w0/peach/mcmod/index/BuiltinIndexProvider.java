package com.github.mouse0w0.peach.mcmod.index;

import com.github.mouse0w0.peach.mcmod.MapColor;

public class BuiltinIndexProvider extends GenericIndexProvider {
    public BuiltinIndexProvider(IndexManager indexManager) {
        super("builtin", 10000);

        indexManager.addProvider(this);

        getIndex(IndexTypes.MAP_COLORS).put(MapColor.INHERIT.getId(), MapColor.INHERIT);
    }
}
