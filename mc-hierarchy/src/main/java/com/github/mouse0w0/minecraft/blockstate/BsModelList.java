package com.github.mouse0w0.minecraft.blockstate;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@JsonAdapter(BsModelList.Persistence.class)
public class BsModelList {
    private final List<BsModel> models;

    public BsModelList() {
        this.models = new ArrayList<>(0);
    }

    public BsModelList(BsModel... models) {
        this.models = new ArrayList<>(models.length);
        Collections.addAll(this.models, models);
    }

    public BsModelList(Collection<BsModel> models) {
        this.models = new ArrayList<>(models);
    }

    public BsModelList(BsModelList other) {
        this.models = new ArrayList<>(other.models);
    }

    private BsModelList(ArrayList<BsModel> models) {
        this.models = models;
    }

    public List<BsModel> getModels() {
        return models;
    }

    public BsModel getModel() {
        return models.size() > 0 ? models.get(0) : null;
    }

    public void setModel(BsModel model) {
        final int size = models.size();
        if (size == 0) {
            models.add(model);
        } else if (size == 1) {
            models.set(0, model);
        } else {
            models.clear();
            models.add(model);
        }
    }

    public static final class Persistence implements JsonSerializer<BsModelList>, JsonDeserializer<BsModelList> {

        @Override
        public BsModelList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonArray()) {
                JsonArray jsonModels = json.getAsJsonArray();
                ArrayList<BsModel> models = new ArrayList<>(jsonModels.size());
                for (JsonElement jsonModel : jsonModels) {
                    models.add(context.deserialize(jsonModel, BsModel.class));
                }
                return new BsModelList(models);
            } else if (json.isJsonObject()) {
                ArrayList<BsModel> models = new ArrayList<>(1);
                models.add(context.deserialize(json, BsModel.class));
                return new BsModelList(models);
            } else {
                return new BsModelList();
            }
        }

        @Override
        public JsonElement serialize(BsModelList src, Type typeOfSrc, JsonSerializationContext context) {
            List<BsModel> models = src.getModels();
            switch (models.size()) {
                case 0:
                    return JsonNull.INSTANCE;
                case 1:
                    return context.serialize(models.get(0));
                default:
                    JsonArray jsonModels = new JsonArray();
                    for (BsModel model : models) {
                        jsonModels.add(context.serialize(model));
                    }
                    return jsonModels;
            }
        }
    }

    @Override
    public String toString() {
        return "BsModelList{" +
                "models=" + models +
                '}';
    }
}
