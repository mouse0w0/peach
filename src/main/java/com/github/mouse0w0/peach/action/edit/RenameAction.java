package com.github.mouse0w0.peach.action.edit;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.dialog.RenameDialog;
import com.github.mouse0w0.peach.util.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RenameAction extends Action {
    @Override
    public void perform(ActionEvent event) {
        Path path = DataKeys.PATH.get(event);
        if (path != null) {
            String fileName = FileUtils.getFileName(path);
            String message = String.format(I18n.translate(
                    Files.isRegularFile(path) ? "dialog.rename.message.file" : "dialog.rename.message.folder"), fileName);
            RenameDialog renameDialog = new RenameDialog(message, fileName);
            String result = renameDialog.showAndWait();
            if (fileName.equals(result)) return;
            try {
                Files.move(path, path.getParent().resolve(result));
            } catch (IOException ignored) {
            }
        }
    }
}
