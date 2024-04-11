package com.github.mouse0w0.peach.mcmod.element.provider;

import com.github.mouse0w0.peach.fileEditor.FileEditor;
import com.github.mouse0w0.peach.mcmod.IdMetadata;
import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.mcmod.ItemData;
import com.github.mouse0w0.peach.mcmod.element.editor.BlockEditor;
import com.github.mouse0w0.peach.mcmod.element.impl.BlockElement;
import com.github.mouse0w0.peach.mcmod.index.IndexKeys;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.index.IndexProvider;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.mcmod.project.ModProjectService;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.project.Project;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.nio.file.Path;
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
        block.setItemGroup(Iterables.getFirst(indexManager.getIndex(IndexKeys.ITEM_GROUP).keys(), null));
        block.setModel(Identifier.of("minecraft:cube_all"));
        block.setItemModel(ModelManager.DEFAULT);
        return block;
    }

    @Override
    public FileEditor newEditor(Project project, BlockElement element) {
        return new BlockEditor(project, element);
    }

    @Override
    public Object[] addIndex(Project project, IndexProvider provider, BlockElement element) {
        if (element.isDoNotRegisterItem()) return null;

        String itemId = ModProjectService.getInstance(project).getModId() + ":" + element.getIdentifier();
        List<ItemData> itemDataList = ImmutableList.of(new ItemData(itemId, 0, 64, 0, true, element.getDisplayName(), ResourceUtils.CUBE_TEXTURE));
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
