package com.github.mouse0w0.peach.action.mainMenu;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.project.ProjectManager;
import com.github.mouse0w0.peach.ui.project.ProjectWindow;
import com.github.mouse0w0.peach.ui.project.WindowManager;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class OpenProjectAction extends Action {
    @Override
    public void perform(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(I18n.translate("ui.main.open_project"));
        ProjectWindow window = WindowManager.getInstance().getFocusedWindow();
        File file = directoryChooser.showDialog(window != null ? window.getStage() : null);
        if (file == null) return;
        ProjectManager.getInstance().openProject(file.toPath());
    }
}
