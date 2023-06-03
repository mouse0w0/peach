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

        int fileCount = 0, directoryCount = 0, symbolicLinkCount = 0;
        for (Path path : paths) {
            if (Files.isRegularFile(path)) {
                fileCount++;
            } else if (Files.isDirectory(path)) {
                directoryCount++;
            } else if (Files.isSymbolicLink(path)) {
                symbolicLinkCount++;
            }
        }
        int mask = 0;
        if (fileCount != 0) mask |= 1;
        if (directoryCount != 0) mask |= 2;
        if (symbolicLinkCount != 0) mask |= 4;

        String translationKey = switch (mask) {
            case 1 -> fileCount == 1 ? "dialog.delete.message.file" : "dialog.delete.message.files";
            case 2 -> directoryCount == 1 ? "dialog.delete.message.directory" : "dialog.delete.message.directories";
            case 3 -> "dialog.delete.message.fileAndDirectory";
            case 4 ->
                    symbolicLinkCount == 1 ? "dialog.delete.message.symbolicLink" : "dialog.delete.message.symbolicLinks";
            case 5 -> "dialog.delete.message.fileAndSymbolicLink";
            case 6 -> "dialog.delete.message.directoryAndSymbolicLink";
            default -> "dialog.delete.message.fileAndDirectoryAndSymbolicLink";
        };

        String message = AppL10n.localize(translationKey, FileUtils.getFileName(paths.get(0)), fileCount, directoryCount, symbolicLinkCount);

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
