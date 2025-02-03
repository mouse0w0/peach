package com.github.mouse0w0.peach.mcmod.element.provider;

import com.github.mouse0w0.peach.fileEditor.FileEditor;
import com.github.mouse0w0.peach.mcmod.IdMetadata;
import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.mcmod.ItemData;
import com.github.mouse0w0.peach.mcmod.element.editor.BlockEditor;
import com.github.mouse0w0.peach.mcmod.element.impl.BlockElement;
import com.github.mouse0w0.peach.mcmod.index.IndexKeys;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.index.Indexer;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.mcmod.preview.PreviewManager;
import com.github.mouse0w0.peach.project.Project;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.nio.file.Path;
import java.util.List;

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
        block.setItemGroup(Iterables.getFirst(indexManager.getIndex(IndexKeys.ITEM_GROUP).keyList(), null));
        block.setModel(Identifier.of("minecraft:cube_all"));
        block.setItemModel(ModelManager.DEFAULT);
        return block;
    }

    @Override
    public FileEditor newEditor(Project project, BlockElement element) {
        return new BlockEditor(project, element);
    }

    @Override
    public void index(Project project, BlockElement element, Indexer indexer) {
        if (element.isDoNotRegisterItem()) return;

        Identifier itemId = Identifier.ofProject(element.getIdentifier());
        List<ItemData> itemDataList = ImmutableList.of(new ItemData(itemId, 0, 64, 0, true, element.getDisplayName(), PreviewManager.getInstance(project).renderBlockItem(element)));
        indexer.add(IndexKeys.ITEM, IdMetadata.of(itemId), itemDataList);
        indexer.add(IndexKeys.ITEM, IdMetadata.ofIgnoreMetadata(itemId), itemDataList);
    }
}
