package com.github.mouse0w0.peach.mcmod.element.provider;

import com.github.mouse0w0.peach.fileEditor.FileEditor;
import com.github.mouse0w0.peach.mcmod.ItemGroup;
import com.github.mouse0w0.peach.mcmod.element.editor.ItemGroupEditor;
import com.github.mouse0w0.peach.mcmod.element.impl.MEItemGroup;
import com.github.mouse0w0.peach.mcmod.index.IndexProvider;
import com.github.mouse0w0.peach.mcmod.index.Indexes;
import com.github.mouse0w0.peach.project.Project;

import java.nio.file.Path;

public class ItemGroupProvider extends ElementProvider<MEItemGroup> {
    public ItemGroupProvider() {
        super(MEItemGroup.class, "group");
    }

    @Override
    public MEItemGroup newElement(Project project, Path file, String identifier, String name) {
        final MEItemGroup itemGroup = new MEItemGroup();
        itemGroup.setFile(file);
        itemGroup.setIdentifier(identifier);
        itemGroup.setDisplayName(name);
        return itemGroup;
    }

    @Override
    public FileEditor newEditor(Project project, MEItemGroup element) {
        return new ItemGroupEditor(project, element);
    }

    @Override
    public Object[] addIndex(Project project, IndexProvider provider, MEItemGroup element) {
        ItemGroup itemGroup = new ItemGroup(element.getIdentifier(), null, element.getIcon());
        itemGroup.setLocalizedText(element.getDisplayName());

        provider.getIndex(Indexes.ITEM_GROUPS).put(element.getIdentifier(), itemGroup);
        return new Object[]{element.getIdentifier()};
    }

    @Override
    public void removeIndex(Project project, IndexProvider provider, Object[] objects) {
        provider.getIndex(Indexes.ITEM_GROUPS).remove(objects[0]);
    }
}
