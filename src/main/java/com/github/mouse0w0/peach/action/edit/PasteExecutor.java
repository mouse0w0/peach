package com.github.mouse0w0.peach.action.edit;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.dialog.Alert;
import com.github.mouse0w0.peach.dialog.ButtonType;
import com.github.mouse0w0.peach.dialog.PasteDialog;
import com.github.mouse0w0.peach.dialog.RenameDialog;
import com.github.mouse0w0.peach.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
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
                Iterator<Path> iterator = Files.list(source).iterator();
                while (iterator.hasNext()) {
                    Path child = iterator.next();
                    handle(child, target.resolve(child.getFileName()));
                }
            }
        } catch (IOException e) {
            LOGGER.error("Failed to paste file because an exception has occurred.", e);
            Alert.error(I18n.translate("dialog.paste.title"), e.toString());
        }
    }

    private void handleFile(Path source, Path target) throws IOException {
        if (source.equals(target)) return;

        while (Files.exists(target)) {
            if (skipAll) return;
            if (overwriteAll) {
                copyOrMove(source, target);
                return;
            }
            ButtonType buttonType = new PasteDialog(I18n.translate("dialog.paste.title"),
                    I18n.format("dialog.paste.message", folder, source.getFileName()), multiple)
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
                target = new RenameDialog(target).showAndWait().orElse(null);
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
