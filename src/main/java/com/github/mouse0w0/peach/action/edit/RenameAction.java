package com.github.mouse0w0.peach.action.edit;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.dialog.Alert;
import com.github.mouse0w0.peach.dialog.RenameDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class RenameAction extends Action {
    private static final Logger LOGGER = LoggerFactory.getLogger(RenameAction.class);

    @Override
    public void perform(ActionEvent event) {
        Path path = DataKeys.PATH.get(event);
        if (path == null) return;

        Optional<Path> result = new RenameDialog(path).showAndWait();
        if (result.isPresent()) {
            Path target = result.get();
            try {
                Files.move(path, target);
            } catch (FileAlreadyExistsException e) {
                Alert.error(I18n.translate("dialog.rename.title"),
                        I18n.format("dialog.rename.error.fileAlreadyExists", target));
            } catch (IOException e) {
                LOGGER.error("Failed to rename file because an exception has occurred.", e);
                Alert.error(I18n.translate("dialog.rename.title"), e.toString());
            }
        }
    }
}
