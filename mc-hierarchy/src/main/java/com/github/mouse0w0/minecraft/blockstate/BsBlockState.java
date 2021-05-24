package com.github.mouse0w0.minecraft.blockstate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.JsonAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonAdapter(BsBlockState.Serializer.class)
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

    public static class Serializer implements JsonSerializer<BsBlockState> {

        @Override
        public JsonElement serialize(BsBlockState src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject root = new JsonObject();
            if (!src.getVariants().isEmpty()) root.add("variants", context.serialize(src.getVariants()));
            if (!src.getMultiparts().isEmpty()) root.add("multiparts", context.serialize(src.getMultiparts()));
            return root;
        }
    }
}
