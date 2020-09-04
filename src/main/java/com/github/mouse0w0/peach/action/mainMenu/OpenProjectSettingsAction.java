package com.github.mouse0w0.peach.action.mainMenu;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.mcmod.project.McModDataKeys;
import com.github.mouse0w0.peach.mcmod.ui.McModSettingsUI;
import com.github.mouse0w0.peach.ui.project.ProjectWindow;
import com.github.mouse0w0.peach.ui.project.WindowManager;

public class OpenProjectSettingsAction extends Action {
    @Override
    public void perform(ActionEvent event) {
        ProjectWindow window = WindowManager.getInstance().getFocusedWindow();
        McModSettingsUI.show(window.getProject().getData(McModDataKeys.MOD_SETTINGS), window.getWindow());
    }
}
