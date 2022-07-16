package com.github.mouse0w0.peach.mcmod.element.provider;

import com.github.mouse0w0.peach.fileEditor.FileEditor;
import com.github.mouse0w0.peach.javafx.util.ImageUtils;
import com.github.mouse0w0.peach.mcmod.EquipmentSlot;
import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.ItemRef;
import com.github.mouse0w0.peach.mcmod.element.editor.ItemEditor;
import com.github.mouse0w0.peach.mcmod.element.impl.MEItem;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.index.IndexProvider;
import com.github.mouse0w0.peach.mcmod.index.Indexes;
import com.github.mouse0w0.peach.mcmod.project.McModDescriptor;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.StringUtils;
import com.google.common.collect.Iterables;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ItemProvider extends ElementProvider<MEItem> {
    public ItemProvider() {
        super(MEItem.class, "item");
    }

    @Override
    public MEItem newElement(Project project, Path file, String identifier, String name) {
        final IndexManager indexManager = IndexManager.getInstance(project);
        final MEItem item = new MEItem();
        item.setFile(file);
        item.setIdentifier(identifier);
        item.setDisplayName(name);
        item.setEquipmentSlot(EquipmentSlot.NONE);
        item.setItemGroup(Iterables.getFirst(indexManager.getIndex(Indexes.ITEM_GROUPS).values(), null));
        item.setModel(new Identifier("minecraft:generated"));
        item.setEquipSound(indexManager.getIndex(Indexes.SOUND_EVENTS).get("minecraft:item.armor.equip_generic"));
        return item;
    }

    @Override
    public FileEditor newEditor(Project project, MEItem element) {
        return new ItemEditor(project, element);
    }

    @Override
    public Object[] addIndex(Project project, IndexProvider provider, MEItem element) {
        String modId = McModDescriptor.getInstance(project).getModId();

        Item item = new Item(element.getIdentifier(), 0, null, false);
        item.setLocalizedText(element.getDisplayName());

        String layer0 = element.getTextures().get("layer0");
        if (StringUtils.isNotEmpty(layer0)) {
            item.setImage(ImageUtils.of(ResourceUtils.getTextureFile(project, layer0), 64, 64, true, false));
        } else {
            item.setImage(ResourceUtils.MISSING_TEXTURE);
        }

        Map<ItemRef, List<Item>> items = provider.getIndex(Indexes.ITEMS);
        ItemRef item1 = ItemRef.createItem(modId + ":" + item.getId(), item.getMetadata());
        items.put(item1, Collections.singletonList(item));
        ItemRef item2 = ItemRef.createIgnoreMetadata(modId + ":" + item.getId());
        items.put(item2, Collections.singletonList(item));
        return new Object[]{item1, item2};
    }

    @Override
    public void removeIndex(Project project, IndexProvider provider, Object[] objects) {
        Map<ItemRef, List<Item>> items = provider.getIndex(Indexes.ITEMS);
        items.remove(objects[0]);
        items.remove(objects[1]);
    }
}
