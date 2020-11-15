package com.github.mouse0w0.peach.file;

import com.github.mouse0w0.peach.ui.icon.Icons;
import com.github.mouse0w0.peach.util.FileUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

import java.nio.file.Files;
import java.nio.file.Path;

public class CommonFileAppearance implements FileAppearance {
    @Override
    public boolean isAcceptable(Path file) {
        return true;
    }

    @Override
    public void apply(Path file, StringProperty text, ObjectProperty<Image> icon) {
        String fileName = FileUtils.getFileName(file);
        text.set(fileName);

        if (Files.isDirectory(file)) {
            icon.set(Icons.File.Folder);
        } else {
            switch (FileUtils.getFileExtension(fileName)) {
                case "png":
                    icon.set(Icons.File.Image);
                    break;
                case "ogg":
                    icon.set(Icons.File.Sound);
                    break;
                case "json":
                    icon.set(Icons.File.Json);
                    break;
                default:
                    icon.set(Icons.File.File);
                    break;
            }
        }
    }
}
