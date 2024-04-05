package com.github.mouse0w0.peach.mcmod.vanillaData;

import com.github.mouse0w0.peach.l10n.L10n;
import com.github.mouse0w0.peach.mcmod.*;
import com.github.mouse0w0.peach.mcmod.index.GenericIndexProvider;
import com.github.mouse0w0.peach.mcmod.index.IndexType;
import com.github.mouse0w0.peach.mcmod.index.IndexTypes;
import com.github.mouse0w0.peach.mcmod.model.BlockstateTemplate;
import com.github.mouse0w0.peach.mcmod.model.ModelTemplate;
import com.github.mouse0w0.peach.plugin.Plugin;
import com.github.mouse0w0.peach.util.ClassPathUtils;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class VanillaDataImpl extends GenericIndexProvider implements VanillaData {
    private final String version;
    private final Plugin plugin;
    private final L10n l10n;

    public VanillaDataImpl(String version, Plugin plugin) {
        super("vanilla", 200);
        this.version = version;
        this.plugin = plugin;
        this.l10n = L10n.getL10n(plugin.getId());
        loadItemData();
        loadOreDictionaryData();
        loadIconicData(IndexTypes.ITEM_GROUP);
        loadIconicData(IndexTypes.MATERIAL);
        loadIconicData(IndexTypes.SOUND_TYPE);
        loadIconicData(IndexTypes.MAP_COLOR);
        loadGameData(IndexTypes.SOUND_EVENT);
        loadGameData(IndexTypes.ENCHANTMENT);
        loadGameData(IndexTypes.ATTRIBUTE);
        loadGameData(IndexTypes.ENCHANTMENT_TYPE);
        loadGameData(IndexTypes.OFFSET_TYPE);
        loadGameData(IndexTypes.AI_PATH_NODE_TYPE);
        loadGameData(IndexTypes.PLANT_TYPE);
        loadGameData(IndexTypes.PUSH_REACTION);
        loadGameData(IndexTypes.RENDER_TYPE);
        loadGameData(IndexTypes.TOOL_TYPE);
        loadGameData(IndexTypes.USE_ANIMATION);
        loadBlockstateTemplates();
        loadModelTemplates();
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    private final Map<String, BlockstateTemplate> blockstateTemplateMap = new HashMap<>();

    @Override
    public Map<String, BlockstateTemplate> getBlockstateTemplateMap() {
        return blockstateTemplateMap;
    }

    private void loadBlockstateTemplates() {
        try {
            Path state = ClassPathUtils.getPath("blockstate", plugin.getClassLoader());
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(state)) {
                for (Path path : stream) {
                    if (FileUtils.getFileName(path).endsWith(".json")) {
                        loadBlockstateTemplate(path);
                    }
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void loadBlockstateTemplate(Path file) {
        try {
            BlockstateTemplate blockstateTemplate = JsonUtils.readJson(file, BlockstateTemplate.class);
            blockstateTemplate.setPlugin(plugin);
            blockstateTemplateMap.put(FileUtils.getFileNameWithoutExtension(file), blockstateTemplate);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    private final Map<Identifier, ModelTemplate> modelTemplateMap = new HashMap<>();

    @Override
    public Map<Identifier, ModelTemplate> getModelTemplateMap() {
        return modelTemplateMap;
    }

    private void loadModelTemplates() {
        try {
            Path model = ClassPathUtils.getPath("model", plugin.getClassLoader());
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(model)) {
                for (Path path : stream) {
                    if (FileUtils.getFileName(path).endsWith(".json")) {
                        loadModelTemplate(path);
                    }
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void loadModelTemplate(Path file) {
        try {
            ModelTemplate modelTemplate = JsonUtils.readJson(file, ModelTemplate.class);
            Identifier modelTemplateId = modelTemplate.getId();
            modelTemplate.setPlugin(plugin);
            modelTemplate.setLocalizedName(l10n.localize("model." + modelTemplateId.getNamespace() + "." + modelTemplateId.getName()));
            modelTemplateMap.put(modelTemplateId, modelTemplate);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void loadItemData() {
        Map<IdMetadata, List<ItemData>> map = getIndex(IndexTypes.ITEM);
        for (JsonElement element : loadRawData("item")) {
            if (element.isJsonObject()) {
                JsonObject object = element.getAsJsonObject();
                String id = object.get("id").getAsString();
                int metadata = object.get("metadata").getAsInt();
                int maxStackSize = object.get("maxStackSize").getAsInt();
                int maxDamage = object.get("maxDamage").getAsInt();
                boolean isBlock = object.get("isBlock").getAsBoolean();
                String name = l10n.localize(getItemTranslationKey(id, metadata));
                Image texture = getItemTexture(id, metadata);
                ItemData itemData = new ItemData(id, metadata, maxStackSize, maxDamage, isBlock, name, texture);
                map.computeIfAbsent(IdMetadata.of(id, metadata), k -> new ArrayList<>()).add(itemData);
                map.computeIfAbsent(IdMetadata.ignoreMetadata(id), k -> new ArrayList<>()).add(itemData);
            }
        }
    }

    private void loadOreDictionaryData() {
        Map<IdMetadata, List<ItemData>> map = getIndex(IndexTypes.ITEM);
        for (JsonElement element : loadRawData("ore_dictionary")) {
            if (element.isJsonObject()) {
                JsonObject object = element.getAsJsonObject();
                String id = object.get("id").getAsString();
                JsonArray entries = object.get("entries").getAsJsonArray();
                List<ItemData> oreDictionaryEntries = map.computeIfAbsent(IdMetadata.oreDictionary(id), k -> new ArrayList<>());
                for (JsonElement entry : entries) {
                    JsonObject entryObject = entry.getAsJsonObject();
                    String entryId = entryObject.get("id").getAsString();
                    int entryMetadata = entryObject.get("metadata").getAsInt();
                    List<ItemData> entryItemDatumData = map.get(IdMetadata.of(entryId, entryMetadata));
                    if (entryItemDatumData != null) {
                        oreDictionaryEntries.addAll(entryItemDatumData);
                    }
                }
            }
        }
    }

    private void loadGameData(IndexType<String, GameData> indexType) {
        Map<String, GameData> map = getIndex(indexType);
        for (JsonElement element : loadRawData(indexType.getName())) {
            String id = element.getAsString();
            map.put(id, new GameData(id, l10n.localize(getTranslationKey(indexType.getLowerCamelName(), id))));
        }
    }

    private void loadIconicData(IndexType<String, IconicData> indexType) {
        Map<String, IconicData> map = getIndex(indexType);
        for (JsonElement element : loadRawData(indexType.getName())) {
            JsonObject object = element.getAsJsonObject();
            String id = object.get("id").getAsString();
            IdMetadata icon = JsonUtils.fromJson(object.get("icon").getAsJsonObject(), IdMetadata.class);
            map.put(id, new IconicData(id, l10n.localize(getTranslationKey(indexType.getLowerCamelName(), id)), icon));
        }
    }

    private JsonArray loadRawData(String name) {
        URL resource = ClassPathUtils.getResource("data/" + name + ".json", plugin.getClassLoader());
        try {
            return JsonUtils.readJson(resource).getAsJsonArray();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String getTranslationKey(String prefix, String identifier) {
        StringBuilder sb = new StringBuilder(prefix).append('.');
        for (int i = 0, len = identifier.length(); i < len; i++) {
            char c = identifier.charAt(i);
            if (c == ':') {
                sb.append('.');
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private String getItemTranslationKey(String identifier, int metadata) {
        StringBuilder sb = new StringBuilder("item.");
        for (int i = 0, len = identifier.length(); i < len; i++) {
            char c = identifier.charAt(i);
            if (c == ':') {
                sb.append('.');
            } else {
                sb.append(c);
            }
        }
        return sb.append('.').append(metadata).toString();
    }

    private Image getItemTexture(String identifier, int metadata) {
        int separatorIndex = identifier.indexOf(':');
        String path = identifier.substring(separatorIndex + 1);
        URL resource = plugin.getClassLoader().getResource("texture/item/" + path + "_" + metadata + ".png");
        if (resource == null) return null;
        try (InputStream inputStream = resource.openStream()) {
            return new Image(inputStream);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
