package com.github.mouse0w0.peach.mcmod.element.provider;

import com.github.mouse0w0.peach.fileEditor.FileEditor;
import com.github.mouse0w0.peach.mcmod.IconicData;
import com.github.mouse0w0.peach.mcmod.element.editor.ItemGroupEditor;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemGroupElement;
import com.github.mouse0w0.peach.mcmod.index.IndexKeys;
import com.github.mouse0w0.peach.mcmod.index.IndexProvider;
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
        provider.getIndex(IndexKeys.ITEM_GROUP).put(element.getIdentifier(), new IconicData(element.getIdentifier(), element.getDisplayName(), element.getIcon()));
        return new Object[]{element.getIdentifier()};
    }

    @Override
    public void removeIndex(Project project, IndexProvider provider, Object[] objects) {
        provider.getIndex(IndexKeys.ITEM_GROUP).remove(objects[0]);
    }
}
