package com.github.mouse0w0.peach.action.file;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.dialog.Alert;
import com.github.mouse0w0.peach.dialog.TextInputDialog;
import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NewFileAction extends Action {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewFileAction.class);

    @Override
    public void update(ActionEvent event) {
        setVisible(DataKeys.PATH.get(event) != null);
    }

    @Override
    public void perform(ActionEvent event) {
        final Path path = DataKeys.PATH.get(event);
        if (path == null) return;

        new TextInputDialog(AppL10n.localize("dialog.newFile.title"), null)
                .showAndWait()
                .ifPresent(directoryName -> {
                    try {
                        Files.createFile(FileUtils.getDirectory(path).resolve(directoryName));
                    } catch (FileAlreadyExistsException e) {
                        Alert.error(AppL10n.localize("dialog.newFile.title"),
                                AppL10n.localize("dialog.newFile.error.alreadyExists", directoryName));
                    } catch (IOException e) {
                        LOGGER.error("Failed to new file because an exception has occurred.", e);
                        Alert.error(AppL10n.localize("dialog.newFile.title"), e.toString());
                    }
                });
    }
}
