package com.github.mouse0w0.peach.mcmod.generator.util;

import com.github.mouse0w0.peach.mcmod.model.BlockstateTemplate;
import com.github.mouse0w0.peach.util.ClassPathUtils;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.URLUtils;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class BlockstateUtils {

    public static void generateBlockstate(BlockstateTemplate blockstateTemplate, String namespace, Map<String, String> models, Path output) throws IOException, TemplateException {
        try (BufferedWriter writer = FileUtils.newBufferedWriter(output)) {
            getBlockstateTemplate(blockstateTemplate).process(processBlockstateModels(namespace, models), writer);
        }
    }

    private static Template getBlockstateTemplate(BlockstateTemplate blockstateTemplate) throws IOException {
        URL url = ClassPathUtils.getResource("blockstate/template/" + blockstateTemplate.getTemplate(), blockstateTemplate.getPlugin().getClassLoader());
        try (BufferedReader reader = URLUtils.newBufferedReader(url)) {
            return new Template(null, reader, null, "UTF-8");        // TODO: cache it
        }
    }

    private static Map<String, String> processBlockstateModels(String namespace, Map<String, String> models) {
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, String> entry : models.entrySet()) {
            String modelKey = entry.getKey();
            String modelPath = entry.getValue();
            if (modelPath.startsWith("block/")) {
                // ”block/".length() == 6
                result.put(modelKey, ModelUtils.processResourcePath(namespace, modelPath.substring(6)));
            }
        }
        return result;
    }
}
