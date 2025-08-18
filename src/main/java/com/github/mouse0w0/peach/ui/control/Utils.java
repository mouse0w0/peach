package com.github.mouse0w0.peach.ui.control;

import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

class Utils {
    public static boolean testExtensionFilters(File file, List<FileChooser.ExtensionFilter> filters) {
        if (filters == null || filters.isEmpty()) {
            return true;
        }

        String fileName = file.getName();
        for (FileChooser.ExtensionFilter filter : filters) {
            for (String extension : filter.getExtensions()) {
                if (extension.equals("*.*")) {
                    return true;
                } else if (extension.startsWith("*.") && fileName.endsWith(extension.substring(1))) {
                    return true;
                } else if (fileName.equals(extension)) {
                    return true;
                }
            }
        }
        return false;
    }
}
