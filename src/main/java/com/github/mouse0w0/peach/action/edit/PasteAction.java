package com.github.mouse0w0.peach.action.edit;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.dialog.Alert;
import com.github.mouse0w0.peach.javafx.ClipboardUtils;
import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.util.FileUtils;
import javafx.scene.input.Clipboard;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class PasteAction extends Action {

    @Override
    public void update(ActionEvent event) {
        setDisable(!Clipboard.getSystemClipboard().hasFiles());
    }

    @Override
    public void perform(ActionEvent event) {
        Path target = DataKeys.PATH.get(event);
        if (target == null) return;
        target = FileUtils.getDirectory(target);

        Clipboard clipboard = Clipboard.getSystemClipboard();
        List<Path> paths = FileUtils.listFileToPath(clipboard.getFiles());
        boolean move = ClipboardUtils.hasTransferMode(clipboard, ClipboardUtils.TRANSFER_MODE_MOVE);

        int fileCount = 0, folderCount = 0;
        for (Path path : paths) {
            if (Files.isRegularFile(path)) {
                fileCount++;
            } else if (Files.isDirectory(path)) {
                if (target.startsWith(path)) {
                    Alert.error(AppL10n.localize("dialog.paste.title"),
                            AppL10n.localize("dialog.paste.error.tryPasteIntoSubdirectory"));
                    return;
                }
                folderCount++;
            }
        }

        final String titleKey;
        final String messageKey;
        if (move) {
            titleKey = "dialog.move.title";
            if (fileCount != 0 && folderCount != 0) {
                messageKey = "dialog.move.message.fileAndFolder";
            } else {
                if (fileCount > 0) {
                    messageKey = fileCount == 1 ? "dialog.move.message.file" : "dialog.move.message.files";
                } else {
                    messageKey = folderCount == 1 ? "dialog.move.message.folder" : "dialog.move.message.folders";
                }
            }
        } else {
            titleKey = "dialog.copy.title";
            if (fileCount != 0 && folderCount != 0) {
                messageKey = "dialog.copy.message.fileAndFolder";
            } else {
                if (fileCount > 0) {
                    messageKey = fileCount == 1 ? "dialog.copy.message.file" : "dialog.copy.message.files";
                } else {
                    messageKey = folderCount == 1 ? "dialog.copy.message.folder" : "dialog.copy.message.folders";
                }
            }
        }

        if (Alert.confirm(AppL10n.localize(titleKey),
                AppL10n.localize(messageKey, FileUtils.getFileName(paths.get(0)), fileCount, folderCount, FileUtils.getFileName(target)))) {
            new PasteExecutor(paths, target, move).run();
        }
    }
}
