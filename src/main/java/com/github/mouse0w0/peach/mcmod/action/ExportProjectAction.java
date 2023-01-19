package com.github.mouse0w0.peach.mcmod.action;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.javafx.util.ExtensionFilters;
import com.github.mouse0w0.peach.mcmod.generator.Generator;
import com.github.mouse0w0.peach.mcmod.project.McModMetadata;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.project.service.FileChooserHelper;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.Scheduler;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class ExportProjectAction extends Action {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportProjectAction.class);

    @Override
    public void perform(ActionEvent event) {
        Project project = DataKeys.PROJECT.get(event);
        if (project == null) return;

        CompletableFuture.supplyAsync(() -> {
            try {
                Generator generator = new Generator(project);
                generator.run();
                return generator;
            } catch (Throwable e) {
                LOGGER.error("Unexpected error.", e);
                throw e;
            }
        }, Scheduler.getInstance()).thenAcceptAsync(generator -> {
            McModMetadata metadata = generator.getMetadata();

            String fileName = metadata.getId() + "-" + metadata.getVersion() + ".jar";

            File file = FileChooserHelper.getInstance().save(null, "mcmod.export", null, fileName,
                    ExtensionFilters.JAR);
            if (file != null) {
                FileUtils.forceCopy(generator.getOutputFolder().resolve("artifacts/" + fileName), file.toPath());
            }
        }, Platform::runLater);
    }
}
