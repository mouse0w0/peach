package com.github.mouse0w0.peach.file;

import com.github.mouse0w0.peach.icon.AppIcon;
import com.github.mouse0w0.peach.util.FileUtils;

import java.nio.file.Files;
import java.nio.file.Path;

public class StandardFileAppearance implements FileAppearance {

    @Override
    public boolean apply(Path file, FileCell cell) {
        String fileName = FileUtils.getFileName(file);
        cell.setText(fileName);

        if (Files.isDirectory(file)) {
            cell.setIcon(AppIcon.File.Folder);
        } else {
            switch (FileUtils.getFileExtension(fileName)) {
                case "png" -> cell.setIcon(AppIcon.File.Image);
                case "ogg" -> cell.setIcon(AppIcon.File.Sound);
                case "json" -> cell.setIcon(AppIcon.File.Json);
                default -> cell.setIcon(AppIcon.File.File);
            }
        }
        return false;
    }
}
