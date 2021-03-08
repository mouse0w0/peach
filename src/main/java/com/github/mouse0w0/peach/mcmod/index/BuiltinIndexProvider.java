package com.github.mouse0w0.peach.mcmod.index;

import com.github.mouse0w0.peach.mcmod.MapColor;

public class BuiltinIndexProvider extends IndexProvider {
    public BuiltinIndexProvider(IndexManager indexManager) {
        super("BUILTIN", 1000);

        indexManager.registerProvider(this);

        getIndex(Indexes.MAP_COLORS).put(MapColor.INHERIT.getId(), MapColor.INHERIT);
    }
}
