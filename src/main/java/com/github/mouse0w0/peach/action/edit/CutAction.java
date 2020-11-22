package com.github.mouse0w0.peach.action.edit;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.data.DataKeys;
import com.google.common.collect.ImmutableList;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class CutAction extends Action {
    @Override
    public void perform(ActionEvent event) {
        List<?> items = DataKeys.SELECTED_ITEMS.get(event);
        if (items == null || items.isEmpty()) return;

        if (items.get(0) instanceof Path) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            List<File> files = items.stream().map(item -> ((Path) item).toFile()).collect(ImmutableList.toImmutableList());
            content.putFiles(files);
            clipboard.setContent(content);
        }
    }
}
