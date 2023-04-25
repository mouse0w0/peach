package com.github.mouse0w0.peach.action.edit;

import com.github.mouse0w0.peach.dialog.Alert;
import com.github.mouse0w0.peach.dialog.ButtonType;
import com.github.mouse0w0.peach.dialog.PasteDialog;
import com.github.mouse0w0.peach.dialog.RenameDialog;
import com.github.mouse0w0.peach.l10n.AppL10n;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class PasteExecutor implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(PasteExecutor.class);

    private static final CopyOption[] REPLACE_EXISTING = {StandardCopyOption.REPLACE_EXISTING};

    private final List<Path> files;
    private final Path folder;

    private final boolean multiple;
    private final boolean move;

    private boolean overwriteAll = false;
    private boolean skipAll = false;

    public PasteExecutor(List<Path> files, Path folder, boolean move) {
        this.files = files;
        this.folder = folder;
        this.multiple = files.size() > 1;
        this.move = move;
    }

    @Override
    public void run() {
        for (Path file : files) {
            handle(file, folder.resolve(file.getFileName()));
        }
    }

    private void handle(Path source, Path target) {
        try {
            handleFile(source, target);

            if (Files.isDirectory(source)) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(source)) {
                    for (Path child : stream) {
                        handle(child, target.resolve(child.getFileName()));
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Failed to paste file because an exception has occurred.", e);
            Alert.error(AppL10n.localize("dialog.paste.title"), e.toString());
        }
    }

    private void handleFile(Path source, Path target) throws IOException {
        if (move && source.equals(target)) return;

        while (Files.exists(target)) {
            if (skipAll) return;
            if (overwriteAll) {
                copyOrMove(source, target);
                return;
            }
            ButtonType buttonType = new PasteDialog(AppL10n.localize("dialog.paste.title"),
                    AppL10n.localize("dialog.paste.error.existsSameFile", folder, source.getFileName()), multiple)
                    .showAndWait().orElse(PasteDialog.SKIP);
            if (buttonType == PasteDialog.SKIP) {
                return;
            } else if (buttonType == PasteDialog.SKIP_ALL) {
                skipAll = true;
                return;
            } else if (buttonType == PasteDialog.OVERWRITE) {
                copyOrMove(source, target);
                return;
            } else if (buttonType == PasteDialog.OVERWRITE_ALL) {
                overwriteAll = true;
                copyOrMove(source, target);
                return;
            } else if (buttonType == PasteDialog.RENAME) {
                target = RenameDialog.create(target).showAndWait().orElse(null);
                if (target == null) return;
            }
        }

        copyOrMove(source, target);
    }

    private void copyOrMove(Path source, Path target) throws IOException {
        if (move) {
            Files.move(source, target, REPLACE_EXISTING);
        } else {
            Files.copy(source, target, REPLACE_EXISTING);
        }
    }
}
