package com.github.mouse0w0.peach.action.file;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.data.DataKeys;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ShowInExplorerAction extends Action {
    @Override
    public void perform(ActionEvent event) {
        Path path = DataKeys.PATH.get(event);
        if (path != null) {
            File file = path.toFile();
            try {
                Desktop.getDesktop().open(file.isFile() ? file.getParentFile() : file);
            } catch (IOException ignored) {
            }
        }
    }
}
