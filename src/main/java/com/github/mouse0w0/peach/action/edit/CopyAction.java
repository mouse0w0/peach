package com.github.mouse0w0.peach.action.edit;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.ui.util.ClipboardUtils;
import com.github.mouse0w0.peach.util.FileUtils;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.nio.file.Path;
import java.util.List;

public class CopyAction extends Action {
    @Override
    public void perform(ActionEvent event) {
        List<Path> paths = DataKeys.PATHS.get(event);
        if (paths == null || paths.isEmpty()) return;
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        ClipboardUtils.setTransferMode(content, ClipboardUtils.TRANSFER_MODE_COPY_OR_LINK);
        content.putFiles(FileUtils.listPathToFile(paths));
        clipboard.setContent(content);
    }
}
