package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.dialog.Alert;
import com.github.mouse0w0.peach.fileEditor.FileEditorManager;
import com.github.mouse0w0.peach.fileWatch.FileChangeListener;
import com.github.mouse0w0.peach.fileWatch.ProjectFileWatcher;
import com.github.mouse0w0.peach.fileWatch.WeakFileChangeListener;
import com.github.mouse0w0.peach.javafx.CachedImage;
import com.github.mouse0w0.peach.mcmod.*;
import com.github.mouse0w0.peach.mcmod.element.impl.MEItem;
import com.github.mouse0w0.peach.mcmod.element.impl.MEItemGroup;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.index.IndexProvider;
import com.github.mouse0w0.peach.mcmod.index.Indexes;
import com.github.mouse0w0.peach.mcmod.project.McModDescriptor;
import com.github.mouse0w0.peach.mcmod.util.ModUtils;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class ElementManager extends IndexProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElementManager.class);

    private final Project project;
    private final McModDescriptor descriptor;
    private final ElementRegistry elementRegistry;
    private final Path sourcesPath;
    private final Path previewPath;
    private final Gson gson;
    private final FileChangeListener fileChangeListener;

    public static ElementManager getInstance(Project project) {
        return project.getService(ElementManager.class);
    }

    public ElementManager(Project project, McModDescriptor descriptor, IndexManager indexManager, ElementRegistry elementRegistry) {
        super("PROJECT", 200);
        this.project = project;
        this.descriptor = descriptor;
        this.elementRegistry = elementRegistry;
        this.sourcesPath = ResourceUtils.getResourcePath(project, ResourceUtils.SOURCES);
        FileUtils.createDirectoriesIfNotExistsSilently(sourcesPath);
        this.previewPath = project.getPath().resolve(".peach/preview");
        FileUtils.createDirectoriesIfNotExistsSilently(previewPath);
        this.gson = new GsonBuilder()
                .registerTypeAdapter(ItemGroup.class, new ItemGroup.Persister(indexManager.getIndex(Indexes.ITEM_GROUPS)))
                .registerTypeAdapter(Material.class, new Material.Persister(indexManager.getIndex(Indexes.MATERIALS)))
                .registerTypeAdapter(SoundType.class, new SoundType.Persister(indexManager.getIndex(Indexes.SOUND_TYPES)))
                .registerTypeAdapter(MapColor.class, new MapColor.Persister(indexManager.getIndex(Indexes.MAP_COLORS)))
                .create();
        indexManager.registerProvider(this);
        this.fileChangeListener = new FileChangeListener() {
            @Override
            public void onFileDelete(ProjectFileWatcher watcher, Path path) {
                clearElement(path);
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
                ElementType<?> elementType = elementRegistry.getElementType(file);
                if (elementType != null) {
                    indexElement(loadElement(file));
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
        ElementType<?> type = elementRegistry.getElementType(file);
        if (type == null) {
            throw new IllegalArgumentException("Cannot load element");
        }
        try {
            Element element = JsonUtils.readJson(gson, file, type.getType());
            Element.setFile(element, file);
            return (T) element;
        } catch (IOException e) {
            LOGGER.error("Failed to load element.", e);
            return (T) type.newInstance(file);
        }
    }

    public void saveElement(Element element) {
        try {
            JsonUtils.writeJson(gson, element.getFile(), element);
        } catch (IOException e) {
            LOGGER.error("Failed to save element.", e);
            //TODO: show dialog
        }

        clearElement(element.getFile());
        indexElement(element);
    }

    public void createElement(Path path, ElementType<?> type, String name) {
        Path file = path.resolve(name + "." + type.getName() + ".json");

        if (Files.exists(file)) {
            Alert.error(I18n.format("validate.existsFile", file.getFileName()));
            return;
        }

        saveElement(type.create(project, file, ModUtils.toIdentifier(name), name));

        FileEditorManager.getInstance(project).open(file);
    }

    private final Map<Path, Object> indexedElements = new HashMap<>();

    private void clearElement(Path file) {
        if (indexedElements.containsKey(file)) {
            ElementType<?> type = ElementRegistry.getInstance().getElementType(file);
            if (type == ElementTypes.ITEM) {
                ItemRef[] items = (ItemRef[]) indexedElements.remove(file);
                for (ItemRef item : items) {
                    getIndex(Indexes.ITEMS).remove(item);
                }
            } else if (type == ElementTypes.ITEM_GROUP) {
                getIndex(Indexes.ITEM_GROUPS).remove((String) indexedElements.remove(file));
            }
        }
    }

    private void indexElement(Element element) {
        if (element instanceof MEItem) {
            MEItem meItem = (MEItem) element;
            Item item = new Item(meItem.getIdentifier(), 0, null, false);
            item.setLocalizedText(meItem.getDisplayName());

            Path previewFile = previewPath.resolve(element.getFileName() + ".png");
            ElementRegistry.getInstance().getElementType(MEItem.class).generatePreview(project, meItem, previewFile);
            CachedImage cachedImage = new CachedImage(previewFile, 64, 64);
            cachedImage.invalidate();
            item.setDisplayImage(cachedImage);

            String namespace = descriptor.getModId();
            ItemRef item1 = ItemRef.createItem(namespace + ":" + item.getId(), item.getMetadata());
            getIndex(Indexes.ITEMS).put(item1, Collections.singletonList(item));
            ItemRef item2 = ItemRef.createIgnoreMetadata(namespace + ":" + item.getId());
            getIndex(Indexes.ITEMS).put(item2, Collections.singletonList(item));
            indexedElements.put(element.getFile(), new ItemRef[]{item1, item2});
        } else if (element instanceof MEItemGroup) {
            MEItemGroup meItemGroup = (MEItemGroup) element;
            ItemGroup itemGroup = new ItemGroup(meItemGroup.getIdentifier(), null, meItemGroup.getIcon());
            itemGroup.setLocalizedText(meItemGroup.getDisplayName());

            getIndex(Indexes.ITEM_GROUPS).put(meItemGroup.getIdentifier(), itemGroup);
            indexedElements.put(element.getFile(), meItemGroup.getIdentifier());
        }
    }
}
