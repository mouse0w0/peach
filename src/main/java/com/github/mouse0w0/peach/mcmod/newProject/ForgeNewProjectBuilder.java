package com.github.mouse0w0.peach.mcmod.newProject;

import com.github.mouse0w0.peach.fileEditor.FileEditorManager;
import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.mcmod.project.ModProjectMetadata;
import com.github.mouse0w0.peach.mcmod.project.ModProjectService;
import com.github.mouse0w0.peach.mcmod.util.ModIdUtils;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.newProject.NewProjectBuilder;
import com.github.mouse0w0.peach.newProject.NewProjectContext;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.control.FilePicker;
import com.github.mouse0w0.peach.ui.util.GridPanes;
import com.github.mouse0w0.peach.util.FileUtils;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.nio.file.Path;

public final class ForgeNewProjectBuilder implements NewProjectBuilder {
    private final GridPane grid = new GridPane();
    private final TextField name = new TextField();
    private final FilePicker path = new FilePicker(FilePicker.Type.OPEN_DIRECTORY);

    public ForgeNewProjectBuilder() {
        grid.getColumnConstraints().addAll(GridPanes.DEFAULT_COLUMN_CONSTRAINTS, GridPanes.HGROW_COLUMN_CONSTRAINTS);
        grid.setHgap(16);
        grid.setVgap(12);
        grid.addRow(0, new Label(AppL10n.localize("dialog.newProject.name")), name);
        grid.addRow(1, new Label(AppL10n.localize("dialog.newProject.path")), path);
    }

    @Override
    public Node getNode() {
        return grid;
    }

    @Override
    public void initialize(NewProjectContext context) {
        name.setText(context.getProjectName());
        path.setPath(context.getProjectDirectory());
    }

    @Override
    public void commitData(NewProjectContext context) {
        context.setProjectName(name.getText());
        context.setProjectDirectory(path.getPath());
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public void setupProject(Project project, NewProjectContext context) {
        ModProjectMetadata metadata = ModProjectService.getInstance(project).getMetadata();
        metadata.setName(context.getProjectName());
        metadata.setId(ModIdUtils.toModId(context.getProjectName()));
        Path projectPath = project.getPath();
        FileUtils.createDirectories(projectPath.resolve(ResourceUtils.ELEMENTS));
        FileUtils.createDirectories(projectPath.resolve(ResourceUtils.BLOCK_MODELS));
        FileUtils.createDirectories(projectPath.resolve(ResourceUtils.ITEM_MODELS));
        FileUtils.createDirectories(projectPath.resolve(ResourceUtils.BLOCK_TEXTURES));
        FileUtils.createDirectories(projectPath.resolve(ResourceUtils.ITEM_TEXTURES));
        FileUtils.createDirectories(projectPath.resolve(ResourceUtils.ARMOR_TEXTURES));
        FileUtils.createDirectories(projectPath.resolve(ResourceUtils.GUI_TEXTURES));
        FileEditorManager.getInstance(project).open(FileUtils.ensureFileExists(projectPath.resolve(ModProjectMetadata.FILE_NAME)));
    }
}
