package com.github.mouse0w0.peach.action.edit;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.dialog.RenameDialog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class RenameAction extends Action {
    @Override
    public void perform(ActionEvent event) {
        Path path = DataKeys.PATH.get(event);
        if (path == null) return;

        Optional<Path> result = new RenameDialog(path).showAndWait();
        if (result.isPresent()) {
            try {
                Files.move(path, result.get());
            } catch (IOException ignored) {
            }
        }
    }
}
