package com.github.mouse0w0.peach.action.edit;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.javafx.ClipboardUtils;
import javafx.scene.input.Clipboard;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class PasteAction extends Action {

    @Override
    public void update(ActionEvent event) {
        getAppearance().setDisable(!Clipboard.getSystemClipboard().hasFiles());
    }

    @Override
    public void perform(ActionEvent event) {
        final Path path = DataKeys.PATH.get(event);
        if (path == null) return;

        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final List<Path> files = clipboard.getFiles().stream().map(File::toPath).collect(Collectors.toList());
        final Path folder = Files.isDirectory(path) ? path : path.getParent();
        final boolean move = ClipboardUtils.hasTransferMode(clipboard, ClipboardUtils.TRANSFER_MODE_MOVE);
        new PasteExecutor(files, folder, move).run();
    }
}
