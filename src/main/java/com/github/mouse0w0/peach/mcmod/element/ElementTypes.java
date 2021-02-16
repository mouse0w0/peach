package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.mcmod.EquipmentSlot;
import com.github.mouse0w0.peach.mcmod.element.editor.*;
import com.github.mouse0w0.peach.mcmod.element.impl.*;
import com.github.mouse0w0.peach.mcmod.element.preview.ItemPreview;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.index.StandardIndexes;
import com.github.mouse0w0.peach.project.ProjectManager;

public final class ElementTypes {
    public static final ElementType<Item> ITEM =
            ElementType.builder("item", Item.class)
                    .createdHandler((element, file, identifier, name) -> {
                        element.setIdentifier(identifier);
                        element.setDisplayName(name);
                        element.setEquipmentSlot(EquipmentSlot.MAINHAND);
                        ProjectManager.getInstance().getProject(file).ifPresent(project ->
                                element.setItemGroup(
                                        IndexManager.getInstance(project)
                                                .getIndex(StandardIndexes.ITEM_GROUPS)
                                                .keySet()
                                                .stream()
                                                .findFirst()
                                                .orElse(null)));
                    })
                    .editorFactory(ItemEditor::new)
                    .previewGenerator(new ItemPreview())
                    .build();
    public static final ElementType<ItemGroup> ITEM_GROUP =
            ElementType.builder("item_group", ItemGroup.class)
                    .createdHandler((element, file, identifier, name) -> {
                        element.setIdentifier(identifier);
                        element.setDisplayName(name);
                    })
                    .editorFactory(ItemGroupEditor::new)
                    .build();
    public static final ElementType<CraftingRecipe> CRAFTING_RECIPE =
            ElementType.builder("crafting", CraftingRecipe.class)
                    .createdHandler((element, file, identifier, name) -> {
                        element.setIdentifier(identifier);
                    })
                    .editorFactory(CraftingRecipeEditor::new)
                    .build();
    public static final ElementType<SmeltingRecipe> SMELTING_RECIPE =
            ElementType.builder("smelting", SmeltingRecipe.class)
                    .editorFactory(SmeltingRecipeEditor::new)
                    .build();
}
