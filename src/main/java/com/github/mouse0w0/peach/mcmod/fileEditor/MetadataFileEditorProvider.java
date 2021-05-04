package com.github.mouse0w0.peach.mcmod.fileEditor;

import com.github.mouse0w0.peach.fileEditor.FileEditor;
import com.github.mouse0w0.peach.fileEditor.FileEditorProvider;
import com.github.mouse0w0.peach.mcmod.project.McModMetadata;
import com.github.mouse0w0.peach.mcmod.project.MetadataFileEditor;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.FileUtils;

import java.nio.file.Path;

public class MetadataFileEditorProvider implements FileEditorProvider {
    @Override
    public boolean accept(Path file) {
        return McModMetadata.FILE_NAME.equals(FileUtils.getFileName(file));
    }

    @Override
    public FileEditor create(Project project, Path file) {
        return new MetadataFileEditor(project, file);
    }
}
