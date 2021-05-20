package com.github.mouse0w0.minecraft.blockstate;

import com.google.gson.JsonElement;

public class BsMultipart {
    private JsonElement when;
    private BsModelList apply;

    public JsonElement getWhen() {
        return when;
    }

    public void setWhen(JsonElement when) {
        this.when = when;
    }

    public BsModelList getApply() {
        return apply;
    }

    public void setApply(BsModelList apply) {
        this.apply = apply;
    }

    @Override
    public String toString() {
        return "BsMultipart{" +
                "when=" + when +
                ", apply=" + apply +
                '}';
    }
}
