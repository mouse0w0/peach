package com.github.mouse0w0.peach.forge.contentPack;

import com.github.mouse0w0.i18n.Translator;
import com.github.mouse0w0.i18n.source.FileTranslationSource;
import com.github.mouse0w0.peach.forge.contentPack.data.ItemData;
import com.github.mouse0w0.peach.forge.contentPack.data.OreDictData;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.google.common.reflect.TypeToken;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public class ContentPack implements Closeable {

    private static final TypeToken<List<ItemData>> LIST_ITEM_DATA_TYPE = new TypeToken<List<ItemData>>() {
    };
    private static final TypeToken<List<OreDictData>> LIST_ORE_DICT_DATA_TYPE = new TypeToken<List<OreDictData>>() {
    };

    private final Path file;
    private final FileSystem fileSystem;
    private final ContentPackMetadata metadata;

    private List<ItemData> itemData;
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
        load();
    }

    private void load() throws IOException {
        itemData = JsonUtils.readJson(getPath("content/" + getId() + "/item.json"), LIST_ITEM_DATA_TYPE);
        itemData.forEach(item -> ItemData.setDisplayImage(item, getPath(String.format("content/%s/image/item/%s_%d.png", getId(), item.getName(), item.getMetadata()))));
        oreDictionaryData = JsonUtils.readJson(getPath("content/" + getId() + "/oreDictionary.json"), LIST_ORE_DICT_DATA_TYPE);
        setLocale(Locale.getDefault());
    }

    public void setLocale(Locale locale) {
        if (translator != null && translator.getLocale() == locale) return;
        translator = Translator.builder()
                .locale(locale)
                .source(new FileTranslationSource(getPath("content/" + getId() + "/lang")))
                .build();
        itemData.forEach(item -> ItemData.setDisplayName(item, translator.translate(item.getTranslationKey())));
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

    public List<ItemData> getItemData() {
        return itemData;
    }

    public List<OreDictData> getOreDictionaryData() {
        return oreDictionaryData;
    }

    @Override
    public void close() throws IOException {
        fileSystem.close();
    }
}
