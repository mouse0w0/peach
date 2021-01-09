package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.mcmod.EquipmentSlot;
import com.github.mouse0w0.peach.mcmod.element.editor.CraftingRecipeEditorFactory;
import com.github.mouse0w0.peach.mcmod.element.editor.ItemEditor;
import com.github.mouse0w0.peach.mcmod.element.editor.ItemGroupEditorFactory;
import com.github.mouse0w0.peach.mcmod.element.editor.SmeltingRecipeEditorFactory;
import com.github.mouse0w0.peach.mcmod.element.impl.CraftingRecipe;
import com.github.mouse0w0.peach.mcmod.element.impl.Item;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemGroup;
import com.github.mouse0w0.peach.mcmod.element.impl.SmeltingRecipe;
import com.github.mouse0w0.peach.mcmod.element.preview.ItemPreview;

public final class ElementTypes {
    public static final ElementType<Item> ITEM =
            ElementType.builder("item", Item.class)
                    .createdHandler((element, file, identifier, name) -> {
                        element.setIdentifier(identifier);
                        element.setDisplayName(name);
//                        element.setItemGroup(""); TODO: Specify a default item group.
                        element.setEquipmentSlot(EquipmentSlot.MAINHAND);
                    })
                    .editorFactory(ItemEditor::new)
                    .previewGenerator(new ItemPreview())
                    .build();
    public static final ElementType<ItemGroup> ITEM_GROUP =
            ElementType.builder("item_group", ItemGroup.class)
                    .editorFactory(new ItemGroupEditorFactory())
                    .build();
    public static final ElementType<CraftingRecipe> CRAFTING_RECIPE =
            ElementType.builder("crafting", CraftingRecipe.class)
                    .editorFactory(new CraftingRecipeEditorFactory())
                    .build();
    public static final ElementType<SmeltingRecipe> SMELTING_RECIPE =
            ElementType.builder("smelting", SmeltingRecipe.class)
                    .editorFactory(new SmeltingRecipeEditorFactory())
                    .build();
}
