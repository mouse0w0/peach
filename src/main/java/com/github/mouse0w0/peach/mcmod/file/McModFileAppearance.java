package com.github.mouse0w0.peach.mcmod.file;

import com.github.mouse0w0.peach.file.FileAppearance;
import com.github.mouse0w0.peach.icon.Icons;
import com.github.mouse0w0.peach.mcmod.element.ElementRegistry;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.StringUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

import java.nio.file.Path;

public class McModFileAppearance implements FileAppearance {
    @Override
    public boolean accept(Path file) {
        String fileName = FileUtils.getFileName(file);
        if ("project.forge.json".equals(fileName)) return true;
        return ElementRegistry.getInstance().getElementType(file) != null;
    }

    @Override
    public void apply(Path file, StringProperty text, ObjectProperty<Image> icon) {
        String fileName = FileUtils.getFileName(file);
        if ("project.forge.json".equals(fileName)) {
            text.set(fileName);
            icon.set(Icons.File.Forge);
        } else {
            text.set(StringUtils.substringBefore(fileName, '.'));
            icon.set(Icons.File.ModElement);
        }
    }
}
