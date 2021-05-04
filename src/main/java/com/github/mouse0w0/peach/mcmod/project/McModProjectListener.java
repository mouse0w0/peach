package com.github.mouse0w0.peach.mcmod.project;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.event.project.ProjectWindowEvent;
import com.github.mouse0w0.peach.fileEditor.FileEditorManager;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.wm.ProjectWindow;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class McModProjectListener {

    public McModProjectListener() {
        Peach.getEventBus().addListener(this::onOpenedProjectWindow);
    }

    private void onOpenedProjectWindow(ProjectWindowEvent.Opened event) {
        ProjectWindow window = event.getWindow();
        Project project = window.getProject();

        McModDescriptor descriptor = McModDescriptor.getInstance(project);

        Path metadataFile = descriptor.getMetadataFile();
        if (Files.notExists(metadataFile)) {
            try {
                Files.createFile(metadataFile);
            } catch (IOException e) {
                // TODO: show alert
                throw new UncheckedIOException(e);
            }
            FileEditorManager.getInstance(project).open(metadataFile);
        }
    }
}
