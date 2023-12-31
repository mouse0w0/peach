package com.github.mouse0w0.peach.mcmod.vanillaData;

import com.github.mouse0w0.peach.l10n.L10n;
import com.github.mouse0w0.peach.mcmod.GameData;
import com.github.mouse0w0.peach.mcmod.IconicData;
import com.github.mouse0w0.peach.mcmod.IdMetadata;
import com.github.mouse0w0.peach.mcmod.ItemData;
import com.github.mouse0w0.peach.mcmod.index.GenericIndexProvider;
import com.github.mouse0w0.peach.mcmod.index.IndexType;
import com.github.mouse0w0.peach.mcmod.index.IndexTypes;
import com.github.mouse0w0.peach.plugin.Plugin;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.ArrayList;
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
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    private void loadItemData() {
        JsonArray array = loadRawData("item");
        if (array == null) return;

        Map<IdMetadata, List<ItemData>> map = getIndex(IndexTypes.ITEM);
        for (JsonElement element : array) {
            if (element.isJsonObject()) {
                JsonObject object = element.getAsJsonObject();
                String id = object.get("id").getAsString();
                int metadata = object.get("metadata").getAsInt();
                int maxStackSize = object.get("maxStackSize").getAsInt();
                int maxDamage = object.get("maxDamage").getAsInt();
                boolean isBlock = object.get("isBlock").getAsBoolean();
                String name = getItemTranslationKey(id, metadata);
                Image texture = getItemTexture(id, metadata);
                ItemData itemData = new ItemData(id, metadata, maxStackSize, maxDamage, isBlock, name, texture);
                map.computeIfAbsent(IdMetadata.of(id, metadata), k -> new ArrayList<>()).add(itemData);
                map.computeIfAbsent(IdMetadata.ignoreMetadata(id), k -> new ArrayList<>()).add(itemData);
            }
        }
    }

    private void loadOreDictionaryData() {
        JsonArray array = loadRawData("ore_dictionary");
        if (array == null) return;

        Map<IdMetadata, List<ItemData>> map = getIndex(IndexTypes.ITEM);
        for (JsonElement element : array) {
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
        JsonArray array = loadRawData(indexType.getName());
        if (array == null) return;

        Map<String, GameData> map = getIndex(indexType);
        for (JsonElement element : array) {
            String id = element.getAsString();
            map.put(id, new GameData(id, l10n.localize(getTranslationKey(indexType.getLowerCamelName(), id))));
        }
    }

    private void loadIconicData(IndexType<String, IconicData> indexType) {
        JsonArray array = loadRawData(indexType.getName());
        if (array == null) return;

        Map<String, IconicData> map = getIndex(indexType);
        for (JsonElement element : array) {
            JsonObject object = element.getAsJsonObject();
            String id = object.get("id").getAsString();
            IdMetadata icon = JsonUtils.fromJson(object.get("icon").getAsJsonObject(), IdMetadata.class);
            map.put(id, new IconicData(id, l10n.localize(getTranslationKey(indexType.getLowerCamelName(), id)), icon));
        }
    }

    private JsonArray loadRawData(String name) {
        URL resource = plugin.getClassLoader().getResource("data/" + name + ".json");
        if (resource == null) return null;
        try {
            JsonElement element = JsonUtils.readJson(resource);
            return element.isJsonArray() ? element.getAsJsonArray() : null;
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
