package com.github.mouse0w0.peach.dialog;

import com.github.mouse0w0.peach.ui.util.CheckItem;
import com.github.mouse0w0.peach.ui.util.Validator;

import java.nio.file.Path;

public class RenameDialogWithValidator extends RenameDialog {

    public RenameDialogWithValidator(Path source, String text, CheckItem<? super String> checkItem) {
        super(source, text);
        Validator.register(getEditor(), checkItem);
    }

    @Override
    public void close() {
        if (getResult() == null || Validator.test(getEditor())) {
            super.close();
        }
    }
}
