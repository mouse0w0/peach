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

public class NewFileAction extends Action {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewFileAction.class);

    @Override
    public void update(ActionEvent event) {
        event.getPresentation().setVisible(DataKeys.PATH.get(event) != null);
    }

    @Override
    public void perform(ActionEvent event) {
        Path path = DataKeys.PATH.get(event);
        if (path == null) return;

        new TextInputDialog(AppL10n.localize("dialog.newFile.title"), null).showAndWait().ifPresent(fileName -> {
            try {
                Path filePath = FileUtils.getDirectory(path).resolve(fileName);
                if (Files.exists(filePath)) {
                    Alert.error(AppL10n.localize("dialog.newFile.title"), AppL10n.localize("dialog.newFile.error.alreadyExists", fileName));
                    return;
                }
                Path parentPath = filePath.getParent();
                if (Files.notExists(parentPath)) {
                    Files.createDirectories(parentPath);
                }
                Files.createFile(filePath);
            } catch (IOException e) {
                LOGGER.error("Failed to new file because an exception has occurred.", e);
                Alert.error(AppL10n.localize("dialog.newFile.title"), e.toString());
            }
        });
    }
}
