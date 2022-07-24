package com.github.mouse0w0.peach.mcmod.project;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.fileEditor.FileEditorManager;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.window.ProjectWindow;
import com.github.mouse0w0.peach.window.ProjectWindowListener;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class McModProjectListener {

    public McModProjectListener() {
        Peach.getInstance().getMessageBus().connect().subscribe(ProjectWindowListener.TOPIC, new ProjectWindowListener() {
            @Override
            public void windowShown(ProjectWindow window) {
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

            @Override
            public void windowHidden(ProjectWindow window) {

            }
        });
    }
}
