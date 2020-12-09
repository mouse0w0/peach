package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.mcmod.element.editor.CraftingRecipeEditorFactory;
import com.github.mouse0w0.peach.mcmod.element.editor.ItemEditor;
import com.github.mouse0w0.peach.mcmod.element.editor.ItemGroupEditorFactory;
import com.github.mouse0w0.peach.mcmod.element.editor.SmeltingRecipeEditorFactory;
import com.github.mouse0w0.peach.mcmod.element.impl.CraftingRecipe;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemElement;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemGroup;
import com.github.mouse0w0.peach.mcmod.element.impl.SmeltingRecipe;
import com.github.mouse0w0.peach.mcmod.element.preview.ItemPreview;

public class ElementTypes {

    public static final ElementType<ItemElement> ITEM =
            new ElementType<>("item", ItemElement.class, ItemEditor::new, new ItemPreview());
    public static final ElementType<ItemGroup> ITEM_GROUP =
            new ElementType<>("item_group", ItemGroup.class, new ItemGroupEditorFactory(), null);
    public static final ElementType<CraftingRecipe> CRAFTING_RECIPE =
            new ElementType<>("crafting_recipe", CraftingRecipe.class, new CraftingRecipeEditorFactory(), null);
    public static final ElementType<SmeltingRecipe> SMELTING_RECIPE =
            new ElementType<>("smelting_recipe", SmeltingRecipe.class, new SmeltingRecipeEditorFactory(), null);
}
