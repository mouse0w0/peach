package com.github.mouse0w0.peach.mcmod.element.provider;

import com.github.mouse0w0.peach.fileEditor.FileEditor;
import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.mcmod.element.editor.BlockEditor;
import com.github.mouse0w0.peach.mcmod.element.impl.MEBlock;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.index.Indexes;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.project.Project;
import com.google.common.collect.Iterables;

import java.nio.file.Path;

public class BlockProvider extends ElementProvider<MEBlock> {
    public BlockProvider() {
        super(MEBlock.class, "block");
    }

    @Override
    public MEBlock newElement(Project project, Path file, String identifier, String name) {
        final IndexManager indexManager = IndexManager.getInstance(project);
        final MEBlock block = new MEBlock();
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
    public FileEditor newEditor(Project project, MEBlock element) {
        return new BlockEditor(project, element);
    }
}
