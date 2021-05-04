package com.github.mouse0w0.peach.file;

import com.github.mouse0w0.peach.icon.Icons;
import com.github.mouse0w0.peach.util.FileUtils;

import java.nio.file.Files;
import java.nio.file.Path;

public class CommonFileAppearance implements FileAppearance {
    @Override
    public boolean accept(Path file) {
        return true;
    }

    @Override
    public void apply(Path file, FileCell cell) {
        String fileName = FileUtils.getFileName(file);
        cell.setText(fileName);

        if (Files.isDirectory(file)) {
            cell.setIcon(Icons.File.Folder);
        } else {
            switch (FileUtils.getFileExtension(fileName)) {
                case "png":
                    cell.setIcon(Icons.File.Image);
                    break;
                case "ogg":
                    cell.setIcon(Icons.File.Sound);
                    break;
                case "json":
                    cell.setIcon(Icons.File.Json);
                    break;
                default:
                    cell.setIcon(Icons.File.File);
                    break;
            }
        }
    }
}
