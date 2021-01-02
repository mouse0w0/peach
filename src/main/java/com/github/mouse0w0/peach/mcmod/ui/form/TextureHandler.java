package com.github.mouse0w0.peach.mcmod.ui.form;

import com.github.mouse0w0.peach.dialog.Alert;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface TextureHandler {

    Logger LOGGER = LoggerFactory.getLogger(TextureHandler.class);

    static TextureHandler of(Path root, Path store) {
        return new TextureHandler() {
            @Override
            public File fromString(String texture) {
                return Strings.isNullOrEmpty(texture) ? null : new File(root.resolve(texture + ".png").toString());
            }

            @Override
            public String toString(File file) {
                if (file == null) return null;
                String s = root.relativize(file.toPath()).toString().replace('\\', '/');
                return s.substring(0, s.length() - ".png".length());
            }

            @Override
            public File copy(File file) {
                Path myFile = file.toPath();

                if (myFile.startsWith(root)) return file;

                try {
                    Path newFile = ResourceUtils.copyToLowerCaseFile(myFile, store.resolve(myFile.getFileName()));
                    return newFile != null ? newFile.toFile() : null;
                } catch (IOException e) {
                    LOGGER.error("Failed to copy file because an exception has occurred.", e);
                    Alert.error("An exception has occurred!");
                    return null;
                }
            }
        };
    }

    static TextureHandler ofKeepExtension(Path root, Path store) {
        return new TextureHandler() {
            @Override
            public File fromString(String texture) {
                return Strings.isNullOrEmpty(texture) ? null : new File(root.resolve(texture).toString());
            }

            @Override
            public String toString(File file) {
                return file == null ? null : root.relativize(file.toPath()).toString().replace('\\', '/');
            }

            @Override
            public File copy(File file) {
                Path myFile = file.toPath();

                if (myFile.startsWith(root)) return file;

                try {
                    Path newFile = ResourceUtils.copyToLowerCaseFile(myFile, store.resolve(myFile.getFileName()));
                    return newFile != null ? newFile.toFile() : null;
                } catch (IOException e) {
                    LOGGER.error("Failed to copy file because an exception has occurred.", e);
                    Alert.error("An exception has occurred!");
                    return null;
                }
            }
        };
    }

    File fromString(String texture);

    String toString(File file);

    File copy(File file);
}
