package com.github.mouse0w0.peach.mcmod.generator.util;

import com.github.mouse0w0.peach.mcmod.model.BlockstateTemplate;
import com.github.mouse0w0.peach.util.ClassPathUtils;
import com.github.mouse0w0.peach.util.FileUtils;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class BlockstateUtils {

    public static void generateBlockstate(BlockstateTemplate blockstateTemplate, String namespace, Map<String, String> models, Path output) throws IOException, TemplateException {
        FileUtils.createFileIfNotExists(output);
        try (BufferedWriter writer = Files.newBufferedWriter(output)) {
            getBlockstateTemplate(blockstateTemplate).process(processBlockstateModels(namespace, models), writer);
        }
    }

    private static Template getBlockstateTemplate(BlockstateTemplate blockstateTemplate) throws IOException {
        Path templateFile = ClassPathUtils.getPath("blockstate/template/" + blockstateTemplate.getTemplate(), blockstateTemplate.getPlugin().getClassLoader());
        try (BufferedReader reader = Files.newBufferedReader(templateFile)) {
            return new Template(null, reader, null, "UTF-8");
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
