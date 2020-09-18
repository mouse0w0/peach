package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.mcmod.wizard.CraftingRecipeWizard;
import com.github.mouse0w0.peach.mcmod.wizard.ItemGroupWizard;
import com.github.mouse0w0.peach.mcmod.wizard.ItemWizard;
import com.github.mouse0w0.peach.mcmod.wizard.SmeltingRecipeWizard;

public class ElementTypes {

    public static final ElementType<ItemElement> ITEM =
            new ElementType<>("item", ItemElement.class, ItemElement::new, ItemWizard::new);
    public static final ElementType<ItemGroup> ITEM_GROUP =
            new ElementType<>("item_group", ItemGroup.class, ItemGroup::new, ItemGroupWizard::new);
    public static final ElementType<CraftingRecipe> CRAFTING_RECIPE =
            new ElementType<>("crafting_recipe", CraftingRecipe.class, CraftingRecipe::new, CraftingRecipeWizard::new);
    public static final ElementType<SmeltingRecipe> SMELTING_RECIPE =
            new ElementType<>("smelting_recipe", SmeltingRecipe.class, SmeltingRecipe::new, SmeltingRecipeWizard::new);
}
