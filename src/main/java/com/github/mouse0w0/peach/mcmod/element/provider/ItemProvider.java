package com.github.mouse0w0.peach.mcmod.element.provider;

import com.github.mouse0w0.peach.fileEditor.FileEditor;
import com.github.mouse0w0.peach.mcmod.IdMetadata;
import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.mcmod.ItemData;
import com.github.mouse0w0.peach.mcmod.element.editor.ItemEditor;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemElement;
import com.github.mouse0w0.peach.mcmod.index.IndexKeys;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.index.Indexer;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.util.ImageUtils;
import com.github.mouse0w0.peach.util.StringUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import javafx.scene.image.Image;

import java.nio.file.Path;
import java.util.List;

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
        item.setItemGroup(Iterables.getFirst(indexManager.getIndex(IndexKeys.ITEM_GROUP).keyList(), null));
        item.setModel(Identifier.of("minecraft:generated"));
        item.setEquipSound("minecraft:item.armor.equip_generic");
        return item;
    }

    @Override
    public FileEditor newEditor(Project project, ItemElement element) {
        return new ItemEditor(project, element);
    }

    @Override
    public void index(Project project, ItemElement element, Indexer indexer) {
        String texture = element.getTextures().get("texture");
        Image image;
        if (StringUtils.isNotEmpty(texture)) {
            image = ImageUtils.of(ResourceUtils.getTextureFile(project, texture), 64, 64, true, false);
        } else {
            image = ResourceUtils.MISSING_TEXTURE;
        }

        Identifier itemId = Identifier.project(element.getIdentifier());
        List<ItemData> itemDataList = ImmutableList.of(new ItemData(itemId, 0, element.getMaxStackSize(), element.getDurability(), false, element.getDisplayName(), image));
        indexer.add(IndexKeys.ITEM, IdMetadata.of(itemId), itemDataList);
        indexer.add(IndexKeys.ITEM, IdMetadata.ignoreMetadata(itemId), itemDataList);
    }
}
