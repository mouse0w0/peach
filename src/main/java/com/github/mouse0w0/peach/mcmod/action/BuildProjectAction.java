package com.github.mouse0w0.peach.mcmod.action;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.mcmod.compiler.Compiler;
import com.github.mouse0w0.peach.mcmod.project.McModSettings;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.project.ProjectWindow;
import com.github.mouse0w0.peach.ui.project.WindowManager;
import com.github.mouse0w0.peach.ui.util.Messages;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class BuildProjectAction extends Action {
    private static final Logger LOGGER = LoggerFactory.getLogger(BuildProjectAction.class);

    @Override
    public void perform(ActionEvent event) {
        ProjectWindow window = WindowManager.getInstance().getFocusedWindow();
        Project project = window.getProject();
        Compiler compiler = new Compiler(project.getPath(), project.getPath().resolve("build"));
        compiler.run();

        McModSettings modSettings = compiler.getModSettings();

        String fileName = modSettings.getId() + "-" + modSettings.getVersion() + ".jar";

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(I18n.translate("dialog.export_to.title"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Jar", "*.jar"));
        fileChooser.setInitialFileName(fileName);
        fileChooser.setInitialDirectory(FileUtils.getUserDirectory());
        File file = fileChooser.showSaveDialog(window.getStage());
        if (file != null) {
            try {
                Files.copy(compiler.getOutputDirectory().resolve("artifacts/" + fileName), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                LOGGER.error("Failed to export file: " + file, e);
                Messages.warning("dialog.export_to.failure");
            }
        }
    }
}
