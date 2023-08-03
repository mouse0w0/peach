package com.github.mouse0w0.peach.action.edit;

import com.github.mouse0w0.peach.dialog.Alert;
import com.github.mouse0w0.peach.dialog.PasteDialog;
import com.github.mouse0w0.peach.dialog.RenameDialog;
import com.github.mouse0w0.peach.javafx.control.ButtonType;
import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class PasteExecutor implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(PasteExecutor.class);

    private final List<Path> paths;
    private final Path target;

    private final boolean multiple;
    private final boolean move;

    private boolean overwriteAll = false;
    private boolean skipAll = false;

    public PasteExecutor(List<Path> paths, Path target, boolean move) {
        this.paths = paths;
        this.target = target;
        this.multiple = paths.size() > 1;
        this.move = move;
    }

    @Override
    public void run() {
        try {
            for (Path path : paths) {
                handle(path, target.resolve(path.getFileName()));
            }
        } catch (IOException e) {
            LOGGER.error("Failed to paste file because an exception has occurred.", e);
            Alert.error(AppL10n.localize("dialog.paste.title"), e.toString());
        }
    }

    private void handle(Path source, Path target) throws IOException {
        handleFile(source, target);

        if (Files.isDirectory(source)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(source)) {
                for (Path child : stream) {
                    handle(child, target.resolve(child.getFileName()));
                }
            }
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
                    AppL10n.localize("dialog.paste.error.existsSameFile", this.target, source.getFileName()), multiple)
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
            Files.move(source, target, FileUtils.REPLACE_EXISTING);
        } else {
            Files.copy(source, target, FileUtils.REPLACE_EXISTING);
        }
    }
}
