package com.github.mouse0w0.peach.mcmod.file;

import com.github.mouse0w0.peach.file.FileAppearance;
import com.github.mouse0w0.peach.file.FileCell;
import com.github.mouse0w0.peach.icon.Icons;
import com.github.mouse0w0.peach.mcmod.element.ElementRegistry;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.StringUtils;

import java.nio.file.Path;

public class McModFileAppearance implements FileAppearance {
    @Override
    public boolean accept(Path file) {
        String fileName = FileUtils.getFileName(file);
        if ("project.forge.json".equals(fileName)) return true;
        return ElementRegistry.getInstance().getElementType(file) != null;
    }

    @Override
    public void apply(Path file, FileCell cell) {
        String fileName = FileUtils.getFileName(file);
        if ("project.forge.json".equals(fileName)) {
            cell.setText(fileName);
            cell.setIcon(Icons.File.Forge);
        } else {
            cell.setText(StringUtils.substringBefore(fileName, '.'));
            cell.setIcon(Icons.File.ModElement);
        }
    }
}
