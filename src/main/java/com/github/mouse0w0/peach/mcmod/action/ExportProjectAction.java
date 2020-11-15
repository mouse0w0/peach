package com.github.mouse0w0.peach.mcmod.action;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.mcmod.compiler.Compiler;
import com.github.mouse0w0.peach.mcmod.project.McModSettings;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.project.ProjectWindow;
import com.github.mouse0w0.peach.ui.project.WindowManager;
import com.github.mouse0w0.peach.ui.util.Alerts;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.Scheduler;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class ExportProjectAction extends Action {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportProjectAction.class);

    @Override
    public void perform(ActionEvent event) {
        Project project = DataKeys.PROJECT.get(event);
        if (project == null) return;

        ProjectWindow window = WindowManager.getInstance().getWindow(project);

        CompletableFuture.supplyAsync(() -> {
            Compiler compiler = new Compiler(project.getPath(), project.getPath().resolve("build"));
            compiler.run();
            return compiler;
        }, Scheduler.computation()).thenAcceptAsync(compiler -> {
            McModSettings modSettings = compiler.getModSettings();

            String fileName = modSettings.getId() + "-" + modSettings.getVersion() + ".jar";

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(I18n.translate("dialog.export_to.title"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Jar", "*.jar"));
            fileChooser.setInitialFileName(fileName);
            fileChooser.setInitialDirectory(SystemUtils.getUserHome());
            File file = fileChooser.showSaveDialog(window.getStage());
            if (file != null) {
                try {
                    Path source = compiler.getOutputDirectory().resolve("artifacts/" + fileName);
                    FileUtils.forceCopy(source, file.toPath());
                } catch (IOException e) {
                    LOGGER.error("Failed to export file: " + file, e);
                    Alerts.warning("dialog.export_to.failure");
                }
            }
        }, Platform::runLater);
    }
}
