package com.github.mouse0w0.peach.mcmod.project;

import com.github.mouse0w0.peach.dispose.Disposable;
import com.github.mouse0w0.peach.mcmod.element.ElementManager;
import com.github.mouse0w0.peach.mcmod.index.BuiltinIndexProvider;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.index.IndexManagerImpl;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.mcmod.preview.PreviewManager;
import com.github.mouse0w0.peach.mcmod.preview.PreviewManagerImpl;
import com.github.mouse0w0.peach.mcmod.vanillaData.VanillaData;
import com.github.mouse0w0.peach.mcmod.vanillaData.VanillaDataManager;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public final class ModProjectService implements Disposable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModProjectService.class);

    private final Project project;
    private final Path metadataFile;

    private ModProjectMetadata metadata;
    private final VanillaData vanillaData;
    private final IndexManagerImpl indexManager;
    private final ElementManager elementManager;
    private final ModelManager modelManager;
    private final PreviewManager previewManager;

    public static ModProjectService getInstance(Project project) {
        return project.getService(ModProjectService.class);
    }

    public ModProjectService(Project project) {
        this.project = project;
        this.metadataFile = project.getPath().resolve(ModProjectMetadata.FILE_NAME);

        try {
            this.metadata = JsonUtils.readJson(metadataFile, ModProjectMetadata.class);
        } catch (NoSuchFileException ignored) {
        } catch (IOException e) {
            LOGGER.error("Failed to load metadata.", e);
        }

        if (metadata == null) {
            this.metadata = new ModProjectMetadata();
        }

        vanillaData = VanillaDataManager.getInstance().getVanillaData(metadata.getMcVersion());
        indexManager = new IndexManagerImpl();
        elementManager = new ElementManager(project, indexManager);
        modelManager = new ModelManager(vanillaData);
        previewManager = new PreviewManagerImpl(project, modelManager);
        indexManager.indexNonProjectEntries(ImmutableList.of(BuiltinIndexProvider.INSTANCE, vanillaData));
    }

    public Project getProject() {
        return project;
    }

    public String getModId() {
        return metadata.getId();
    }

    public Path getMetadataFile() {
        return metadataFile;
    }

    public ModProjectMetadata getMetadata() {
        return metadata;
    }

    public VanillaData getVanillaData() {
        return vanillaData;
    }

    public ElementManager getElementManager() {
        return elementManager;
    }

    public IndexManager getIndexManager() {
        return indexManager;
    }

    public ModelManager getModelManager() {
        return modelManager;
    }

    public PreviewManager getPreviewManager() {
        return previewManager;
    }

    public void saveMetadata() {
        try {
            JsonUtils.writeJson(metadataFile, metadata);
        } catch (IOException e) {
            LOGGER.error("Failed to write metadata.", e);
        }
    }

    @Override
    public void dispose() {
        saveMetadata();
    }
}
