package com.github.mouse0w0.peach.action.edit;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.ui.dialog.Alert;
import com.github.mouse0w0.peach.ui.util.ClipboardUtils;
import com.github.mouse0w0.peach.util.FileUtils;
import javafx.scene.input.Clipboard;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class PasteAction extends Action {

    @Override
    public void update(ActionEvent event) {
        event.getPresentation().setDisable(!Clipboard.getSystemClipboard().hasFiles());
    }

    @Override
    public void perform(ActionEvent event) {
        Path target = DataKeys.PATH.get(event);
        if (target == null) return;
        target = FileUtils.getDirectory(target);

        Clipboard clipboard = Clipboard.getSystemClipboard();
        List<Path> paths = FileUtils.listFileToPath(clipboard.getFiles());
        boolean move = ClipboardUtils.hasTransferMode(clipboard, ClipboardUtils.TRANSFER_MODE_MOVE);

        int fileCount = 0, directoryCount = 0;
        for (Path path : paths) {
            if (Files.isRegularFile(path)) {
                fileCount++;
            } else if (Files.isDirectory(path)) {
                if (target.startsWith(path)) {
                    Alert.error(AppL10n.localize("dialog.paste.title"), AppL10n.localize("dialog.paste.error.pasteIntoSubdirectory"));
                    return;
                }
                directoryCount++;
            } else if (Files.isSymbolicLink(path)) {
                Alert.error(AppL10n.localize("dialog.paste.title"), AppL10n.localize("dialog.paste.error.pasteSymbolicLink", path));
                return;
            }
        }

        String titleKey;
        String messageKey;
        if (move) {
            titleKey = "dialog.move.title";
            if (fileCount != 0 && directoryCount != 0) {
                messageKey = "dialog.move.message.fileAndDirectory";
            } else {
                if (fileCount > 0) {
                    messageKey = fileCount == 1 ? "dialog.move.message.file" : "dialog.move.message.files";
                } else {
                    messageKey = directoryCount == 1 ? "dialog.move.message.directory" : "dialog.move.message.directories";
                }
            }
        } else {
            titleKey = "dialog.copy.title";
            if (fileCount != 0 && directoryCount != 0) {
                messageKey = "dialog.copy.message.fileAndDirectory";
            } else {
                if (fileCount > 0) {
                    messageKey = fileCount == 1 ? "dialog.copy.message.file" : "dialog.copy.message.files";
                } else {
                    messageKey = directoryCount == 1 ? "dialog.copy.message.directory" : "dialog.copy.message.directories";
                }
            }
        }

        if (Alert.confirm(AppL10n.localize(titleKey),
                AppL10n.localize(messageKey, FileUtils.getFileName(paths.get(0)), fileCount, directoryCount, FileUtils.getFileName(target)))) {
            new PasteExecutor(paths, target, fileCount > 1 || directoryCount > 0, move).run();
        }
    }
}
