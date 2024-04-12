package com.github.mouse0w0.peach.mcmod.element.provider;

import com.github.mouse0w0.peach.fileEditor.FileEditor;
import com.github.mouse0w0.peach.mcmod.IdMetadata;
import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.mcmod.ItemData;
import com.github.mouse0w0.peach.mcmod.element.editor.ItemEditor;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemElement;
import com.github.mouse0w0.peach.mcmod.index.IndexKeys;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.index.IndexProvider;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.util.ImageUtils;
import com.github.mouse0w0.peach.util.StringUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import javafx.scene.image.Image;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class ItemProvider extends ElementProvider<ItemElement> {
    public ItemProvider() {
        super(ItemElement.class, "item");
    }

    @Override
    public ItemElement newElement(Project project, Path file, String identifier, String name) {
        final IndexManager indexManager = IndexManager.getInstance(project);
        final ItemElement item = new ItemElement();
        item.setFile(file);
        item.setIdentifier(identifier);
        item.setDisplayName(name);
        item.setItemGroup(Iterables.getFirst(indexManager.getIndex(IndexKeys.ITEM_GROUP).keys(), null));
        item.setModel(Identifier.of("minecraft:generated"));
        item.setEquipSound("minecraft:item.armor.equip_generic");
        return item;
    }

    @Override
    public FileEditor newEditor(Project project, ItemElement element) {
        return new ItemEditor(project, element);
    }

    @Override
    public Object[] addIndex(Project project, IndexProvider provider, ItemElement element) {
        String texture = element.getTextures().get("texture");
        Image image;
        if (StringUtils.isNotEmpty(texture)) {
            image = ImageUtils.of(ResourceUtils.getTextureFile(project, texture), 64, 64, true, false);
        } else {
            image = ResourceUtils.MISSING_TEXTURE;
        }

        Identifier itemId = Identifier.project(element.getIdentifier());
        List<ItemData> itemDataList = ImmutableList.of(new ItemData(itemId, 0, element.getMaxStackSize(), element.getDurability(), false, element.getDisplayName(), image));
        Map<IdMetadata, List<ItemData>> items = provider.getIndex(IndexKeys.ITEM);
        IdMetadata item1 = IdMetadata.of(itemId);
        items.put(item1, itemDataList);
        IdMetadata item2 = IdMetadata.ignoreMetadata(itemId);
        items.put(item2, itemDataList);
        return new Object[]{item1, item2};
    }

    @Override
    public void removeIndex(Project project, IndexProvider provider, Object[] objects) {
        Map<IdMetadata, List<ItemData>> items = provider.getIndex(IndexKeys.ITEM);
        items.remove(objects[0]);
        items.remove(objects[1]);
    }
}
