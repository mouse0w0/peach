package com.github.mouse0w0.peach.ui.control;

import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

class Utils {

    public static boolean checkExtensions(File file, List<FileChooser.ExtensionFilter> filters) {
        if (filters == null || filters.isEmpty()) {
            return true;
        }

        String fileName = file.getName();
        for (FileChooser.ExtensionFilter filter : filters) {
            for (String extension : filter.getExtensions()) {
                if (extension.startsWith("*.")) {
                    if (extension.equals("*.*")) {
                        return true;
                    }
                    if (fileName.endsWith(extension.substring(2))) {
                        return true;
                    }
                } else {
                    if (fileName.equals(extension)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
