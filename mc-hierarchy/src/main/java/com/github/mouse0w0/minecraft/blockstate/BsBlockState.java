package com.github.mouse0w0.minecraft.blockstate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BsBlockState {
    private Map<String, BsModelList> variants = new HashMap<>();
    private List<BsMultipart> multiparts = new ArrayList<>();

    public Map<String, BsModelList> getVariants() {
        return variants;
    }

    public List<BsMultipart> getMultiparts() {
        return multiparts;
    }

    @Override
    public String toString() {
        return "BsBlockState{" +
                "variants=" + variants +
                ", multiparts=" + multiparts +
                '}';
    }
}
