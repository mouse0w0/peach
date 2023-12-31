package com.github.mouse0w0.peach.mcmod.vanillaData;

import com.github.mouse0w0.peach.util.WeakValueMap;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VanillaDataManagerImpl implements VanillaDataManager {
    private final Map<String, VanillaDataEP> dataExtensions = new HashMap<>();
    private final WeakValueMap<String, VanillaData> data = new WeakValueMap<>(new ConcurrentHashMap<>());

    public VanillaDataManagerImpl() {
        for (VanillaDataEP extension : VanillaDataEP.EXTENSION_POINT.getExtensions()) {
            dataExtensions.put(extension.getVersion(), extension);
        }
    }

    @Override
    public VanillaData getVanillaData(String version) {
        return data.computeIfAbsent(version, this::loadVanillaData);
    }

    private VanillaData loadVanillaData(String version) {
        VanillaDataEP vanillaDataEP = dataExtensions.get(version);
        if (vanillaDataEP == null) {
            throw new IllegalArgumentException("Not found vanilla data for version " + version);
        }

        return new VanillaDataImpl(version, vanillaDataEP.getPlugin());
    }
}
