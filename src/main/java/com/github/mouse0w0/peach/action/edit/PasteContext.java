package com.github.mouse0w0.peach.action.edit;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.dialog.Alert;
import com.github.mouse0w0.peach.dialog.ButtonType;
import com.github.mouse0w0.peach.dialog.PasteDialog;
import com.github.mouse0w0.peach.dialog.RenameDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class PasteContext implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasteContext.class);

    private static final CopyOption[] REPLACE_EXISTING = {StandardCopyOption.REPLACE_EXISTING};

    private final List<Path> files;
    private final Path folder;

    private final boolean multiple;
    private final boolean move;

    private final Function<Path, Path> renameHandler;

    private boolean overwriteAll = false;
    private boolean skipAll = false;

    public PasteContext(List<Path> files, Path folder, boolean move) {
        this(files, folder, move, null);
    }

    public PasteContext(List<Path> files, Path folder, boolean move, Function<Path, Path> renameHandler) {
        this.files = files;
        this.folder = folder;
        this.multiple = files.size() > 1;
        this.move = move;
        this.renameHandler = renameHandler;
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
            if (!overwriteAll) {
                ButtonType buttonType = new PasteDialog(I18n.translate("dialog.paste.title"),
                        I18n.format("dialog.paste.message", folder, source.getFileName()), multiple)
                        .showAndWait().orElse(PasteDialog.SKIP);
                if (buttonType == PasteDialog.SKIP) {
                    return;
                } else if (buttonType == PasteDialog.SKIP_ALL) {
                    skipAll = true;
                    return;
                } else if (buttonType == PasteDialog.OVERWRITE_ALL) {
                    overwriteAll = true;
                } else if (buttonType == PasteDialog.RENAME) {
                    target = rename(target);
                    continue;
                }
                forceCopyOrMove(source, target);
            }
        }

        copyOrMove(source, target);
    }

    private Path rename(Path target) {
        if (renameHandler == null) {
            return new RenameDialog(target).showAndWait().orElse(target);
        } else {
            return renameHandler.apply(target);
        }
    }

    private void forceCopyOrMove(Path source, Path target) throws IOException {
        if (move) {
            Files.move(source, target, REPLACE_EXISTING);
        } else {
            Files.copy(source, target, REPLACE_EXISTING);
        }
    }

    private void copyOrMove(Path source, Path target) throws IOException {
        if (move) {
            Files.move(source, target);
        } else {
            Files.copy(source, target);
        }
    }
}
