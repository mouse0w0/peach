package com.github.mouse0w0.peach.action.file;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.dialog.Alert;
import com.github.mouse0w0.peach.dialog.TextInputDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NewDirectoryAction extends Action {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewDirectoryAction.class);

    @Override
    public void perform(ActionEvent event) {
        final Path path = DataKeys.PATH.get(event);
        if (path == null) return;

        final Path folder = Files.isDirectory(path) ? path : path.getParent();

        new TextInputDialog(I18n.translate("dialog.newDirectory.title"), null)
                .showAndWait()
                .ifPresent(directoryName -> {
                    try {
                        Files.createDirectories(folder.resolve(directoryName));
                    } catch (FileAlreadyExistsException e) {
                        Alert.error(I18n.translate("dialog.newDirectory.title"),
                                I18n.format("dialog.newDirectory.error.alreadyExists", directoryName));
                    } catch (IOException e) {
                        LOGGER.error("Failed to new directory because an exception has occurred.", e);
                        Alert.error(I18n.translate("dialog.newDirectory.title"), e.toString());
                    }
                });
    }
}
