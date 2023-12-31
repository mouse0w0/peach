package com.github.mouse0w0.peach.mcmod.vanillaData;

import com.github.mouse0w0.peach.Peach;

public interface VanillaDataManager {
    static VanillaDataManager getInstance() {
        return Peach.getInstance().getService(VanillaDataManager.class);
    }

    VanillaData getVanillaData(String version);
}
