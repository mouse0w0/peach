package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.fileEditor.FileEditorManager;
import com.github.mouse0w0.peach.fileWatch.FileChangeListener;
import com.github.mouse0w0.peach.fileWatch.ProjectFileWatcher;
import com.github.mouse0w0.peach.fileWatch.WeakFileChangeListener;
import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.mcmod.*;
import com.github.mouse0w0.peach.mcmod.element.provider.ElementProvider;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.index.IndexProvider;
import com.github.mouse0w0.peach.mcmod.index.Indexes;
import com.github.mouse0w0.peach.mcmod.util.ModUtils;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.dialog.Alert;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public final class ElementManager extends IndexProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElementManager.class);

    private final Project project;
    private final ElementRegistry registry;
    private final Path sourcesPath;
    private final Gson gson;
    private final FileChangeListener fileChangeListener;

    public static ElementManager getInstance(Project project) {
        return project.getService(ElementManager.class);
    }

    public ElementManager(Project project, IndexManager indexManager, ElementRegistry registry) {
        super("PROJECT", 200);
        this.project = project;
        this.registry = registry;
        this.sourcesPath = ResourceUtils.getResourcePath(project, ResourceUtils.SOURCES);
        FileUtils.createDirectoriesIfNotExists(sourcesPath);
        indexManager.registerProvider(this);
        this.gson = new GsonBuilder()
                .registerTypeAdapter(ItemGroup.class, new ItemGroup.Persister(indexManager.getIndex(Indexes.ITEM_GROUPS)))
                .registerTypeAdapter(Material.class, new Material.Persister(indexManager.getIndex(Indexes.MATERIALS)))
                .registerTypeAdapter(SoundType.class, new SoundType.Persister(indexManager.getIndex(Indexes.SOUND_TYPES)))
                .registerTypeAdapter(MapColor.class, new MapColor.Persister(indexManager.getIndex(Indexes.MAP_COLORS)))
                .registerTypeAdapter(SoundEvent.class, new SoundEvent.Persister(indexManager.getIndex(Indexes.SOUND_EVENTS)))
                .create();
        this.fileChangeListener = new FileChangeListener() {
            @Override
            public void onFileDelete(ProjectFileWatcher watcher, Path path) {
                removeIndex(path);
            }
        };
        ProjectFileWatcher.getInstance(project).addListener(new WeakFileChangeListener(fileChangeListener));
        indexElements();
    }

    private void indexElements() {
        try {
            Iterator<Path> iterator = Files.walk(sourcesPath).iterator();
            while (iterator.hasNext()) {
                Path file = iterator.next();
                ElementProvider<?> provider = registry.getElementProvider(file);
                if (provider != null) {
                    addIndex(loadElement(file));
                }
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load elements.", e);
        }
    }

    public Project getProject() {
        return project;
    }

    @SuppressWarnings("unchecked")
    public <T extends Element> T loadElement(Path file) {
        ElementProvider<?> provider = registry.getElementProvider(file);
        if (provider == null) {
            throw new IllegalArgumentException("Cannot load element");
        }
        try {
            Element element = JsonUtils.readJson(gson, file, provider.getType());
            element.setFile(file);
            return (T) element;
        } catch (IOException e) {
            LOGGER.error("Failed to load element.", e);
            throw new RuntimeException();
            // TODO: show dialog
        }
    }

    public void saveElement(Element element) {
        try {
            JsonUtils.writeJson(gson, element.getFile(), element);
        } catch (IOException e) {
            LOGGER.error("Failed to save element.", e);
            // TODO: show dialog
        }

        removeIndex(element.getFile());
        addIndex(element);
    }

    public void createElement(Path path, ElementProvider<?> provider, String name) {
        Path file = path.resolve(name + "." + provider.getName() + ".json");

        if (Files.exists(file)) {
            Alert.error(AppL10n.localize("validate.existsFile", file.getFileName()));
            return;
        }

        saveElement(provider.newElement(project, file, ModUtils.toIdentifier(name), name));

        FileEditorManager.getInstance(project).open(file);
    }

    private final Map<Path, Object[]> indexData = new HashMap<>();

    public void addIndex(Element element) {
        ElementProvider provider = registry.getElementProvider(element.getClass());
        Object[] objects = provider.addIndex(project, this, element);
        if (objects == null) return;
        indexData.put(element.getFile(), objects);
    }

    public void removeIndex(Path file) {
        Object[] objects = indexData.get(file);
        if (objects == null) return;
        ElementProvider provider = registry.getElementProvider(file);
        provider.removeIndex(project, this, objects);
    }
}
