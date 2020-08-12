package com.github.mouse0w0.peach.mcmod.model;

import com.github.mouse0w0.peach.mcmod.model.json.JsonModel;
import com.github.mouse0w0.peach.mcmod.model.json.JsonModelHelper;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.RuntimeIOException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ModelManager {
    private final Map<String, JsonModel> itemModels = new HashMap<>();

    public static ModelManager getInstance(Project project) {
        return project.getService(ModelManager.class);
    }

    public ModelManager() {
        loadBuildInModels();
    }

    private void loadBuildInModels() {
        try {
            loadItemModel(ModelManager.class.getResourceAsStream("/model/generated.json"), "generated");
            loadItemModel(ModelManager.class.getResourceAsStream("/model/handheld.json"), "handheld");
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    public Map<String, JsonModel> getItemModels() {
        return itemModels;
    }

    public JsonModel getItemModel(String name) {
        return itemModels.get(name);
    }

    public void loadItemModel(Path file) throws IOException {
        String modelName = FileUtils.getFileNameWithoutExtensionName(file);
        JsonModel model = JsonModelHelper.load(file);
        itemModels.put(modelName, model);
    }

    private void loadItemModel(InputStream in, String modelName) throws IOException {
        JsonModel model = JsonModelHelper.load(in);
        itemModels.put(modelName, model);
    }
}
