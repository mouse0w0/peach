package com.github.mouse0w0.peach.mcmod.contentPack;

import com.github.mouse0w0.i18n.Translator;
import com.github.mouse0w0.i18n.source.FileTranslationSource;
import com.github.mouse0w0.peach.mcmod.contentPack.data.CreativeTabData;
import com.github.mouse0w0.peach.mcmod.contentPack.data.ItemData;
import com.github.mouse0w0.peach.mcmod.contentPack.data.OreDictData;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.github.mouse0w0.version.VersionRange;
import com.google.common.reflect.TypeToken;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ContentPack implements Closeable {

    private static final TypeToken<List<ItemData>> LIST_ITEM_DATA_TYPE = new TypeToken<List<ItemData>>() {
    };
    private static final TypeToken<List<CreativeTabData>> LIST_CREATIVE_TAB_DATA_TYPE = new TypeToken<List<CreativeTabData>>() {
    };
    private static final TypeToken<List<OreDictData>> LIST_ORE_DICT_DATA_TYPE = new TypeToken<List<OreDictData>>() {
    };

    private final Path file;
    private final FileSystem fileSystem;
    private final ContentPackMetadata metadata;

    private final List<Dependency> dependencies;

    private List<ItemData> itemData;
    private List<CreativeTabData> creativeTabData;
    private List<OreDictData> oreDictionaryData;
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
        this.dependencies = createDependencies(metadata);
        load();
    }

    private void load() throws IOException {
        itemData = JsonUtils.readJson(getPath("content/" + getId() + "/item.json"), LIST_ITEM_DATA_TYPE);
        itemData.forEach(item -> item.setDisplayImage(getPath(String.format("content/%s/image/item/%s_%d.png", getId(), item.getName(), item.getMetadata()))));
        creativeTabData = JsonUtils.readJson(getPath("content/" + getId() + "/creativeTabs.json"), LIST_CREATIVE_TAB_DATA_TYPE);
        oreDictionaryData = JsonUtils.readJson(getPath("content/" + getId() + "/oreDictionary.json"), LIST_ORE_DICT_DATA_TYPE);
        setLocale(Locale.getDefault());
    }

    private List<Dependency> createDependencies(ContentPackMetadata metadata) {
        List<Dependency> dependencies = new ArrayList<>();

        if (!"minecraft".equals(metadata.getId())) {
            dependencies.add(new Dependency("minecraft", VersionRange.createFromVersion(metadata.getMcVersion()), true, false, true));
        }

        String dependenciesString = metadata.getDependencies();
        if (dependenciesString != null) {
            for (String dependency : dependenciesString.split(";")) {
                dependencies.add(Dependency.parse(dependency));
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
        itemData.forEach(item -> item.setDisplayName(translator.translate(item.getTranslationKey())));
        creativeTabData.forEach(creativeTab -> creativeTab.setDisplayName(translator.translate(creativeTab.getTranslationKey())));
    }

    public Path getFile() {
        return file;
    }

    public Path getPath(String first, String... more) {
        return fileSystem.getPath(first, more);
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

    public List<Dependency> getDependencies() {
        return dependencies;
    }

    public List<ItemData> getItemData() {
        return itemData;
    }

    public List<CreativeTabData> getCreativeTabData() {
        return creativeTabData;
    }

    public List<OreDictData> getOreDictionaryData() {
        return oreDictionaryData;
    }

    @Override
    public void close() throws IOException {
        fileSystem.close();
    }
}
