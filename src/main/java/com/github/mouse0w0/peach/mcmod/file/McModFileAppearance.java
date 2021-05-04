package com.github.mouse0w0.peach.mcmod.file;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.file.FileAppearance;
import com.github.mouse0w0.peach.file.FileCell;
import com.github.mouse0w0.peach.icon.Icons;
import com.github.mouse0w0.peach.mcmod.element.ElementRegistry;
import com.github.mouse0w0.peach.mcmod.element.ElementType;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class McModFileAppearance implements FileAppearance {

    private final Map<Path, String> translationKeyMap;

    public McModFileAppearance() {
        translationKeyMap = new HashMap<>();
        translationKeyMap.put(Paths.get("build"), "mod.folder.build");
        translationKeyMap.put(Paths.get("sources"), "mod.folder.source");
        translationKeyMap.put(Paths.get("resources"), "mod.folder.resource");
        translationKeyMap.put(Paths.get("resources/textures"), "mod.folder.texture");
        translationKeyMap.put(Paths.get("resources/textures/armors"), "mod.folder.texture.armor");
        translationKeyMap.put(Paths.get("resources/textures/blocks"), "mod.folder.texture.block");
        translationKeyMap.put(Paths.get("resources/textures/items"), "mod.folder.texture.item");
    }

    @Override
    public boolean apply(Path file, FileCell cell) {
        if (Files.isDirectory(file)) {
            for (Path path : translationKeyMap.keySet()) {
                if (file.endsWith(path)) {
                    cell.setText(I18n.translate(translationKeyMap.get(path)));
                    cell.setIcon(Icons.File.Folder);
                    return true;
                }
            }
        } else {
            String fileName = FileUtils.getFileName(file);
            if ("project.forge.json".equals(fileName)) {
                cell.setText(I18n.translate("mod.file.metadata"));
                cell.setIcon(Icons.File.Forge);
                return true;
            } else {
                ElementType<?> type = ElementRegistry.getInstance().getElementType(file);
                if (type != null) {
                    cell.setText(StringUtils.substringBefore(fileName, '.'));
                    cell.setIcon(Icons.File.ModElement);
                    return true;
                }
            }
        }
        return false;
    }
}
