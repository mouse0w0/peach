package com.github.mouse0w0.peach.action.mainMenu;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.mcmod.ui.NewModElementUI;
import com.github.mouse0w0.peach.ui.project.WindowManager;

public class NewModElementAction extends Action {
    @Override
    public void perform(ActionEvent event) {
        NewModElementUI.show(WindowManager.getInstance().getFocusedWindow().getStage());
    }
}
