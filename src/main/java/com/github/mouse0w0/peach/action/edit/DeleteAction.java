package com.github.mouse0w0.peach.action.edit;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.dialog.Alert;
import com.github.mouse0w0.peach.fileEditor.FileEditorManager;
import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.util.FileUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DeleteAction extends Action {
    @Override
    @SuppressWarnings("unchecked")
    public void perform(ActionEvent event) {
        List<?> items = DataKeys.SELECTED_ITEMS.get(event);
        if (items == null || items.isEmpty()) return;

        if (!(items.get(0) instanceof Path)) return;

        List<Path> paths = (List<Path>) items;

        int fileCount = 0, directoryCount = 0;
        for (Path path : paths) {
            if (Files.isRegularFile(path)) {
                fileCount++;
            } else if (Files.isDirectory(path)) {
                directoryCount++;
            }
        }

        String translationKey;
        if (fileCount != 0 && directoryCount != 0) {
            translationKey = "dialog.delete.message.fileAndDirectories";
        } else {
            if (fileCount > 0) {
                translationKey = fileCount == 1 ? "dialog.delete.message.file" : "dialog.delete.message.files";
            } else {
                translationKey = directoryCount == 1 ? "dialog.delete.message.directory" : "dialog.delete.message.directories";
            }
        }

        String message = AppL10n.localize(translationKey, FileUtils.getFileName(paths.get(0)), fileCount, directoryCount);

        if (Alert.confirm(AppL10n.localize("dialog.delete.title"), message)) {
            FileEditorManager fileEditorManager = FileEditorManager.getInstance(event.getData(DataKeys.PROJECT));

            for (Path path : paths) {
                if (Files.isRegularFile(path)) {
                    fileEditorManager.close(path);
                }
                FileUtils.delete(path);
            }
        }
    }
}
