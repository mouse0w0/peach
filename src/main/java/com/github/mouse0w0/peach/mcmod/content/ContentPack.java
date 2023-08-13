package com.github.mouse0w0.peach.mcmod.content;

import com.github.mouse0w0.i18n.Translator;
import com.github.mouse0w0.i18n.source.FileTranslationSource;
import com.github.mouse0w0.peach.mcmod.*;
import com.github.mouse0w0.peach.ui.util.ImageUtils;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.github.mouse0w0.version.VersionRange;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import javafx.scene.image.Image;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ContentPack implements Closeable {

    private final Path file;
    private final FileSystem fileSystem;
    private final ContentPackMetadata metadata;

    private final List<ContentPackDependency> dependencies;

    private final Map<Class<?>, List<?>> data = new HashMap<>();

    private Translator translator;

    public static ContentPack load(Path file) throws IOException {
        if (!Files.exists(file)) {
            throw new FileNotFoundException(file.toString());
        }

        if (!Files.isRegularFile(file)) {
            throw new IllegalArgumentException("File must be regular file");
        }

        FileSystem fileSystem = FileSystems.newFileSystem(file, Thread.currentThread().getContextClassLoader());
        ContentPackMetadata metadata = JsonUtils.readJson(fileSystem.getPath("content.metadata.json"), ContentPackMetadata.class);
        return new ContentPack(file, fileSystem, metadata);
    }

    protected ContentPack(Path file, FileSystem fileSystem, ContentPackMetadata metadata) throws IOException {
        this.file = file;
        this.fileSystem = fileSystem;
        this.metadata = metadata;
        this.dependencies = resolveDependencies(metadata);
        load();
    }

    private void load() throws IOException {
        load(Item.class, "item.json");
        load(ItemGroup.class, "itemGroup.json");
        load(OreDict.class, "oreDictionary.json");
        load(Material.class, "material.json");
        load(SoundType.class, "soundType.json");
        load(MapColor.class, "mapColor.json");
        load(SoundEvent.class, "soundEvent.json");
        getData(Item.class).forEach(item -> item.setImage(getImage(item)));
        setLocale(Locale.getDefault());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void load(Class<?> clazz, String fileName) throws IOException {
        Path file = getPath("content/" + getId() + "/" + fileName);
        if (!Files.exists(file)) return;
        JsonArray elements = JsonUtils.readJson(file, JsonArray.class);
        List dataList = new ArrayList<>(elements.size());
        for (JsonElement element : elements) {
            dataList.add(JsonUtils.fromJson(element, clazz));
        }
        data.put(clazz, dataList);
    }

    private List<ContentPackDependency> resolveDependencies(ContentPackMetadata metadata) {
        List<ContentPackDependency> dependencies = new ArrayList<>();

        if (!"minecraft".equals(metadata.getId())) {
            dependencies.add(new ContentPackDependency("minecraft", VersionRange.createFromVersion(metadata.getMcVersion()), true, false, true));
        }

        String dependenciesString = metadata.getDependencies();
        if (dependenciesString != null) {
            for (String dependency : dependenciesString.split(";")) {
                dependencies.add(ContentPackDependency.parse(dependency));
            }
        }

        return dependencies;
    }

    public void setLocale(Locale locale) {
        if (translator != null && translator.getLocale() == locale) return;
        translator = Translator.builder()
                .locale(locale)
                .source(new FileTranslationSource(getPath("content/" + getId() + "/lang")))
                .build();
        getData(Item.class).forEach(item -> item.setLocalizedText(translator.translate(item.getTranslationKey(), item.getId())));
        getData(ItemGroup.class).forEach(itemGroup -> itemGroup.setLocalizedText(translator.translate(itemGroup.getTranslationKey(), itemGroup.getId())));
        getData(Material.class).forEach(material -> material.setLocalizedText(translator.translate(material.getTranslationKey(), material.getId())));
        getData(SoundType.class).forEach(soundType -> soundType.setLocalizedText(translator.translate(soundType.getTranslationKey(), soundType.getId())));
        getData(MapColor.class).forEach(mapColor -> mapColor.setLocalizedText(translator.translate(mapColor.getTranslationKey(), mapColor.getId())));
        getData(SoundEvent.class).forEach(soundEvent -> soundEvent.setLocalizedText(translator.translate(soundEvent.getTranslationKey(), soundEvent.getId())));
    }

    public Path getFile() {
        return file;
    }

    public Path getPath(String first, String... more) {
        return fileSystem.getPath(first, more);
    }

    public Image getImage(Item item) {
        return ImageUtils.of(getImageFile(item), 64, 64, true, false);
    }

    public Path getImageFile(Item item) {
        return getPath("content", getId(), "image/item", item.getName() + "_" + item.getMetadata() + ".png");
    }

    public ContentPackMetadata getMetadata() {
        return metadata;
    }

    public String getId() {
        return metadata.getId();
    }

    public String getVersion() {
        return metadata.getVersion();
    }

    public String getMcVersion() {
        return metadata.getMcVersion();
    }

    public List<ContentPackDependency> getDependencies() {
        return dependencies;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getData(Class<T> clazz) {
        List<?> dataList = data.get(clazz);
        return dataList != null ? (List<T>) dataList : Collections.emptyList();
    }

    @Override
    public void close() throws IOException {
        fileSystem.close();
    }
}
