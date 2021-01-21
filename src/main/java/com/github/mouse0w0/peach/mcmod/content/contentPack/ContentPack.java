package com.github.mouse0w0.peach.mcmod.content.contentPack;

import com.github.mouse0w0.i18n.Translator;
import com.github.mouse0w0.i18n.source.FileTranslationSource;
import com.github.mouse0w0.peach.javafx.util.CachedImage;
import com.github.mouse0w0.peach.mcmod.content.data.ItemData;
import com.github.mouse0w0.peach.mcmod.content.data.ItemGroupData;
import com.github.mouse0w0.peach.mcmod.content.data.OreDictData;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.github.mouse0w0.version.VersionRange;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

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
        load(ItemData.class, "item.json");
        load(ItemGroupData.class, "itemGroup.json");
        load(OreDictData.class, "oreDictionary.json");
        getData(ItemData.class).forEach(item -> item.setDisplayImage(getImage(item)));
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
        getData(ItemData.class).forEach(item -> item.setDisplayName(translator.translate(item.getTranslationKey())));
        getData(ItemGroupData.class).forEach(creativeTab -> creativeTab.setDisplayName(translator.translate(creativeTab.getTranslationKey())));
    }

    public Path getFile() {
        return file;
    }

    public Path getPath(String first, String... more) {
        return fileSystem.getPath(first, more);
    }

    public CachedImage getImage(ItemData item) {
        return new CachedImage(getImageFile(item), 64, 64);
    }

    public Path getImageFile(ItemData item) {
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
