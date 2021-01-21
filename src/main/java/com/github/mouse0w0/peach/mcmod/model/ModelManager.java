package com.github.mouse0w0.peach.mcmod.model;

import com.github.mouse0w0.peach.mcmod.model.mcj.McjModel;
import com.github.mouse0w0.peach.mcmod.model.mcj.McjModelHelper;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.RuntimeIOException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ModelManager {
    private final Map<String, McjModel> itemModels = new HashMap<>();

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

    public Map<String, McjModel> getItemModels() {
        return itemModels;
    }

    public McjModel getItemModel(String name) {
        return itemModels.get(name);
    }

    public void loadItemModel(Path file) throws IOException {
        String modelName = FileUtils.getFileNameWithoutExt(file);
        McjModel model = McjModelHelper.load(file);
        itemModels.put(modelName, model);
    }

    private void loadItemModel(InputStream in, String modelName) throws IOException {
        McjModel model = McjModelHelper.load(in);
        itemModels.put(modelName, model);
    }
}
