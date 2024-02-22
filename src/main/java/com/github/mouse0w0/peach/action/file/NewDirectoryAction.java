package com.github.mouse0w0.peach.action.file;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.ui.dialog.Alert;
import com.github.mouse0w0.peach.ui.dialog.TextInputDialog;
import com.github.mouse0w0.peach.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NewDirectoryAction extends Action {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewDirectoryAction.class);

    @Override
    public void update(ActionEvent event) {
        event.getPresentation().setVisible(DataKeys.PATH.get(event) != null);
    }

    @Override
    public void perform(ActionEvent event) {
        Path path = DataKeys.PATH.get(event);
        if (path == null) return;

        new TextInputDialog(AppL10n.localize("dialog.newDirectory.title"), null).showAndWait().ifPresent(directoryName -> {
                    try {
                        Path directoryPath = FileUtils.getDirectory(path).resolve(directoryName);
                        if (Files.exists(directoryPath)) {
                            Alert.error(AppL10n.localize("dialog.newDirectory.title"), AppL10n.localize("dialog.newDirectory.error.alreadyExists", directoryName));
                        }
                        Files.createDirectories(directoryPath);
                    } catch (IOException e) {
                        LOGGER.error("Failed to new directory because an exception has occurred.", e);
                        Alert.error(AppL10n.localize("dialog.newDirectory.title"), e.toString());
                    }
                });
    }
}
