package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.mcmod.EquipmentSlot;
import com.github.mouse0w0.peach.mcmod.element.editor.*;
import com.github.mouse0w0.peach.mcmod.element.impl.*;
import com.github.mouse0w0.peach.mcmod.element.preview.ItemPreview;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.index.Indexes;
import com.google.common.collect.Iterables;

public final class ElementTypes {
    public static final ElementType<MEBlock> BLOCK =
            ElementType.builder("block", MEBlock.class)
                    .createdHandler((project, element, file, identifier, name) -> {
                        element.setIdentifier(identifier);
                        element.setDisplayName(name);
                        element.setItemGroup(Iterables.getFirst(IndexManager.getInstance(project)
                                .getIndex(Indexes.ITEM_GROUPS).values(), null));
                    })
                    .editorFactory(BlockEditor::new)
                    .build();
    public static final ElementType<MEItem> ITEM =
            ElementType.builder("item", MEItem.class)
                    .createdHandler((project, element, file, identifier, name) -> {
                        element.setIdentifier(identifier);
                        element.setDisplayName(name);
                        element.setEquipmentSlot(EquipmentSlot.MAINHAND);
                        element.setItemGroup(Iterables.getFirst(IndexManager.getInstance(project)
                                .getIndex(Indexes.ITEM_GROUPS).values(), null));
                    })
                    .editorFactory(ItemEditor::new)
                    .previewGenerator(new ItemPreview())
                    .build();
    public static final ElementType<MEItemGroup> ITEM_GROUP =
            ElementType.builder("item_group", MEItemGroup.class)
                    .createdHandler((project, element, file, identifier, name) -> {
                        element.setIdentifier(identifier);
                        element.setDisplayName(name);
                    })
                    .editorFactory(ItemGroupEditor::new)
                    .build();
    public static final ElementType<MECraftingRecipe> CRAFTING_RECIPE =
            ElementType.builder("crafting", MECraftingRecipe.class)
                    .createdHandler((project, element, file, identifier, name) -> {
                        element.setIdentifier(identifier);
                    })
                    .editorFactory(CraftingRecipeEditor::new)
                    .build();
    public static final ElementType<MESmeltingRecipe> SMELTING_RECIPE =
            ElementType.builder("smelting", MESmeltingRecipe.class)
                    .editorFactory(SmeltingRecipeEditor::new)
                    .build();
}
