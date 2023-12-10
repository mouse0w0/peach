package com.github.mouse0w0.peach.mcmod.element.provider;

import com.github.mouse0w0.peach.fileEditor.FileEditor;
import com.github.mouse0w0.peach.mcmod.ItemGroup;
import com.github.mouse0w0.peach.mcmod.element.editor.ItemGroupEditor;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemGroupElement;
import com.github.mouse0w0.peach.mcmod.index.IndexProvider;
import com.github.mouse0w0.peach.mcmod.index.IndexTypes;
import com.github.mouse0w0.peach.project.Project;

import java.nio.file.Path;

public class ItemGroupProvider extends ElementProvider<ItemGroupElement> {
    public ItemGroupProvider() {
        super(ItemGroupElement.class, "group");
    }

    @Override
    public ItemGroupElement newElement(Project project, Path file, String identifier, String name) {
        final ItemGroupElement itemGroup = new ItemGroupElement();
        itemGroup.setFile(file);
        itemGroup.setIdentifier(identifier);
        itemGroup.setDisplayName(name);
        return itemGroup;
    }

    @Override
    public FileEditor newEditor(Project project, ItemGroupElement element) {
        return new ItemGroupEditor(project, element);
    }

    @Override
    public Object[] addIndex(Project project, IndexProvider provider, ItemGroupElement element) {
        ItemGroup itemGroup = new ItemGroup(element.getIdentifier(), null, element.getIcon());
        itemGroup.setLocalizedText(element.getDisplayName());

        provider.getIndex(IndexTypes.ITEM_GROUPS).put(element.getIdentifier(), itemGroup);
        return new Object[]{element.getIdentifier()};
    }

    @Override
    public void removeIndex(Project project, IndexProvider provider, Object[] objects) {
        provider.getIndex(IndexTypes.ITEM_GROUPS).remove(objects[0]);
    }
}
