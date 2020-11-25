package com.github.mouse0w0.peach.action.edit;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.dialog.Alert;
import javafx.scene.input.Clipboard;

public class PasteAction extends Action {
    @Override
    public void update(ActionEvent event) {
        getAppearance().setDisable(!Clipboard.getSystemClipboard().hasFiles());
    }

    @Override
    public void perform(ActionEvent event) {
        Alert.error("Unsupported operation");
    }
}
