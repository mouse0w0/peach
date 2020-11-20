package com.github.mouse0w0.peach.action.file;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;

public class ExitAction extends Action {
    @Override
    public void perform(ActionEvent event) {
        Peach.getInstance().exit();
    }
}
