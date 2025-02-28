package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.fileEditor.FileEditorManager;
import com.github.mouse0w0.peach.fileWatch.FileChangeListener;
import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.mcmod.element.provider.ElementProvider;
import com.github.mouse0w0.peach.mcmod.index.IndexKey;
import com.github.mouse0w0.peach.mcmod.index.IndexManagerEx;
import com.github.mouse0w0.peach.mcmod.index.Indexer;
import com.github.mouse0w0.peach.mcmod.project.ModProjectService;
import com.github.mouse0w0.peach.mcmod.util.IdentifierUtils;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.dialog.Alert;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@SuppressWarnings({"rawtypes", "unchecked"})
public final class ElementManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElementManager.class);

    private final ElementRegistry registry = ElementRegistry.getInstance();

    private final Project project;
    private final IndexManagerEx indexManager;
    private final Path sourcesPath;

    public static ElementManager getInstance(Project project) {
        return ModProjectService.getInstance(project).getElementManager();
    }

    public ElementManager(Project project, IndexManagerEx indexManager) {
        this.project = project;
        this.indexManager = indexManager;
        this.sourcesPath = ResourceUtils.getResourcePath(project, ResourceUtils.ELEMENTS);
        project.getMessageBus().connect().subscribe(FileChangeListener.TOPIC, new FileChangeListener() {

            @Override
            public void onFileCreate(Path path) {
                if (path.startsWith(sourcesPath) && FileUtils.size(path) != 0) {
                    ElementProvider<?> provider = registry.getElementProvider(path);
                    if (provider != null) {
                        index(loadElement(path));
                    }
                }
            }

            @Override
            public void onFileDelete(Path path) {
                invalidate(path);
            }
        });
        CompletableFuture.runAsync(() -> ElementManager.getInstance(project).indexElements());
    }

    private void indexElements() {
        if (Files.notExists(sourcesPath)) return;
        try (Stream<Path> stream = Files.walk(sourcesPath)) {
            Iterator<Path> iterator = stream.iterator();
            while (iterator.hasNext()) {
                Path file = iterator.next();
                ElementProvider<?> provider = registry.getElementProvider(file);
                if (provider != null) {
                    index(loadElement(file));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Exception occurred while indexing elements.", e);
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
            Element element = JsonUtils.readJson(file, provider.getType());
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
            JsonUtils.writeJson(element.getFile(), element);
        } catch (IOException e) {
            LOGGER.error("Failed to save element.", e);
            // TODO: show dialog
        }

        invalidate(element.getFile());
        index(element);
    }

    public void createElement(Path path, ElementProvider<?> provider, String name) {
        Path file = path.resolve(name + "." + provider.getName() + ".json");

        if (Files.exists(file)) {
            Alert.error(AppL10n.localize("validate.existsFile", file.getFileName()));
            return;
        }

        saveElement(provider.newElement(project, file, IdentifierUtils.toIdentifier(name), name));

        FileEditorManager.getInstance(project).open(file);
    }

    private final Multimap<Path, IndexEntry<?, ?>> indexEntries = ArrayListMultimap.create(12, 1);

    private void index(Element element) {
        ElementProvider provider = registry.getElementProvider(element.getClass());
        provider.index(project, element, new Indexer() {
            @Override
            public <K, V> void add(IndexKey<K, V> indexKey, K key, V value) {
                indexManager.getIndexEx(indexKey).addProjectEntry(key, value);
                indexEntries.put(element.getFile(), new IndexEntry<>(indexKey, key));
            }
        });
    }

    private void invalidate(Path file) {
        for (IndexEntry<?, ?> indexEntry : indexEntries.removeAll(file)) {
            indexManager.getIndexEx(indexEntry.indexKey).removeProjectEntry(indexEntry.key);
        }
    }

    private static final class IndexEntry<K, V> {
        private final IndexKey<K, V> indexKey;
        private final K key;

        public IndexEntry(IndexKey<K, V> indexKey, K key) {
            this.indexKey = indexKey;
            this.key = key;
        }
    }
}
