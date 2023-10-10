package com.github.mouse0w0.peach.mcmod.element.provider;

import com.github.mouse0w0.peach.fileEditor.FileEditor;
import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.ItemRef;
import com.github.mouse0w0.peach.mcmod.element.editor.BlockEditor;
import com.github.mouse0w0.peach.mcmod.element.impl.BlockElement;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.index.IndexProvider;
import com.github.mouse0w0.peach.mcmod.index.Indexes;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.mcmod.project.ModProjectService;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.project.Project;
import com.google.common.collect.Iterables;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BlockProvider extends ElementProvider<BlockElement> {
    public BlockProvider() {
        super(BlockElement.class, "block");
    }

    @Override
    public BlockElement newElement(Project project, Path file, String identifier, String name) {
        final IndexManager indexManager = IndexManager.getInstance(project);
        final BlockElement block = new BlockElement();
        block.setFile(file);
        block.setIdentifier(identifier);
        block.setDisplayName(name);
        block.setItemGroup(Iterables.getFirst(indexManager.getIndex(Indexes.ITEM_GROUPS).values(), null));
        block.setMaterial(indexManager.getIndex(Indexes.MATERIALS).get("minecraft:rock"));
        block.setSoundType(indexManager.getIndex(Indexes.SOUND_TYPES).get("minecraft:stone"));
        block.setModel(new Identifier("minecraft:cube_all"));
        block.setItemModel(ModelManager.INHERIT);
        return block;
    }

    @Override
    public FileEditor newEditor(Project project, BlockElement element) {
        return new BlockEditor(project, element);
    }

    @Override
    public Object[] addIndex(Project project, IndexProvider provider, BlockElement element) {
        if (element.isDoNotRegisterItem()) return null;

        String modId = ModProjectService.getInstance(project).getModId();

        Item item = new Item(element.getIdentifier(), 0, null, false);
        item.setLocalizedText(element.getDisplayName());
        item.setImage(ResourceUtils.CUBE_TEXTURE);

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
