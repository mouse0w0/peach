package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.dialog.Alert;
import com.github.mouse0w0.peach.fileEditor.FileEditorManager;
import com.github.mouse0w0.peach.mcmod.ItemRef;
import com.github.mouse0w0.peach.mcmod.content.data.ItemData;
import com.github.mouse0w0.peach.mcmod.content.data.ItemGroupData;
import com.github.mouse0w0.peach.mcmod.element.impl.Item;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemGroup;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.index.IndexProvider;
import com.github.mouse0w0.peach.mcmod.index.StandardIndexes;
import com.github.mouse0w0.peach.mcmod.project.McModDescriptor;
import com.github.mouse0w0.peach.mcmod.util.ModUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.util.CachedImage;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.JsonUtils;
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

    private final Path root;

    private final Path previewCache;

    private ElementView elementView;

    public static ElementManager getInstance(Project project) {
        return project.getService(ElementManager.class);
    }

    public ElementManager(Project project, McModDescriptor descriptor, ElementRegistry elementRegistry) {
        super("PROJECT", 200);
        this.project = project;
        this.descriptor = descriptor;
        this.elementRegistry = elementRegistry;
        this.root = project.getPath().resolve("sources");

        this.previewCache = project.getPath().resolve(".peach/preview");
        FileUtils.createDirectoriesIfNotExistsSilently(previewCache);

        IndexManager.getInstance(project).registerProvider(this);
        init();
    }

    private void init() {
        try {
            FileUtils.createDirectoriesIfNotExists(root);
            Iterator<Path> iterator = Files.walk(root).iterator();
            while (iterator.hasNext()) {
                Path file = iterator.next();
                ElementType<?> elementType = elementRegistry.getElementType(file);
                if (elementType != null) {
                    onUpdatedElement(loadElement(file));
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
            Element element = JsonUtils.readJson(file, type.getType());
            Element.setFile(element, file);
            return (T) element;
        } catch (IOException e) {
            return (T) type.newInstance(file);
        }
    }

    public void saveElement(Element element) {
        try {
            JsonUtils.writeJson(element.getFile(), element);
        } catch (IOException e) {
            LOGGER.warn("Failed to save element.", e);
            //TODO: show dialog
        }

        onUpdatedElement(element);
    }

    public void removeElement(Path file) {
        if (!file.startsWith(root)) {
            LOGGER.warn("Try to remove an unmanaged element {}.", file);
            return;
        }

        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            LOGGER.warn("Failed to delete element file.", e);
        }

        onRemovedElement(file);
    }

    public void createAndEditElement(ElementType<?> type, String name) {
        Path file = root.resolve(name + "." + type.getName() + ".json");

        if (Files.exists(file)) {
            Alert.error(I18n.format("validate.existsFile", file.getFileName()));
            return;
        }

        saveElement(type.create(file, ModUtils.toIdentifier(name), name));

        FileEditorManager.getInstance(project).open(file);
    }

    private final Map<Path, Object> cachedElement = new HashMap<>();

    private void onRemovedElement(Path file) {
        if (!cachedElement.containsKey(file)) return;
        ElementType<?> type = ElementRegistry.getInstance().getElementType(file);
        if (type == ElementTypes.ITEM) {
            ItemRef[] items = (ItemRef[]) cachedElement.remove(file);
            for (ItemRef item : items) {
                getIndex(StandardIndexes.ITEMS).remove(item);
            }
        } else if (type == ElementTypes.ITEM_GROUP) {
            getIndex(StandardIndexes.ITEM_GROUPS).remove((String) cachedElement.remove(file));
        }
    }

    private void onUpdatedElement(Element element) {
        Path file = element.getFile();
        onRemovedElement(file);
        if (element instanceof Item) {
            Item itemElement = (Item) element;
            ItemData itemData = new ItemData(itemElement.getIdentifier(), 0, null, false);
            itemData.setDisplayName(itemElement.getDisplayName());

            Path previewFile = previewCache.resolve(element.getFileName() + ".png");
            ElementRegistry.getInstance().getElementType(Item.class).generatePreview(project, itemElement, previewFile);
            CachedImage cachedImage = new CachedImage(previewFile, 64, 64);
            cachedImage.invalidate();
            itemData.setDisplayImage(cachedImage);

            String namespace = descriptor.getModId();
            ItemRef item = ItemRef.createItem(namespace + ":" + itemData.getId(), itemData.getMetadata());
            getIndex(StandardIndexes.ITEMS).put(item, Collections.singletonList(itemData));
            ItemRef ignoreMetadata = ItemRef.createIgnoreMetadata(namespace + ":" + itemData.getId());
            getIndex(StandardIndexes.ITEMS).put(ignoreMetadata, Collections.singletonList(itemData));
            cachedElement.put(file, new ItemRef[]{item, ignoreMetadata});
        } else if (element instanceof ItemGroup) {
            ItemGroup itemGroup = (ItemGroup) element;
            ItemGroupData itemGroupData = new ItemGroupData(itemGroup.getIdentifier(), null, itemGroup.getIcon());
            itemGroupData.setDisplayName(itemGroup.getDisplayName());

            getIndex(StandardIndexes.ITEM_GROUPS).put(itemGroup.getIdentifier(), itemGroupData);
            cachedElement.put(file, itemGroup.getIdentifier());
        }
    }

    public ElementView getElementView() {
        if (elementView == null) {
            elementView = new ElementView(this);
        }
        return elementView;
    }
}
