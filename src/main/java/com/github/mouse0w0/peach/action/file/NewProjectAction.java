package com.github.mouse0w0.peach.action.file;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.javafx.newProject.NewProjectUI;
import com.github.mouse0w0.peach.javafx.util.FXUtils;

public class NewProjectAction extends Action {
    @Override
    public void perform(ActionEvent event) {
        NewProjectUI.show(FXUtils.getFocusedWindow().orElse(null));
    }
}
