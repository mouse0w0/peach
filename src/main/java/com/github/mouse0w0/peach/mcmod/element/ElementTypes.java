package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.mcmod.EquipmentSlot;
import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.mcmod.element.editor.*;
import com.github.mouse0w0.peach.mcmod.element.impl.*;
import com.github.mouse0w0.peach.mcmod.element.preview.ItemPreview;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.index.Indexes;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.google.common.collect.Iterables;

public final class ElementTypes {
    public static final ElementType<MEBlock> BLOCK =
            ElementType.builder("block", MEBlock.class)
                    .createdHandler((project, element, file, identifier, name) -> {
                        final IndexManager indexManager = IndexManager.getInstance(project);
                        element.setIdentifier(identifier);
                        element.setDisplayName(name);
                        element.setItemGroup(Iterables.getFirst(indexManager.getIndex(Indexes.ITEM_GROUPS).values(), null));
                        element.setMaterial(indexManager.getIndex(Indexes.MATERIALS).get("minecraft:rock"));
                        element.setSoundType(indexManager.getIndex(Indexes.SOUND_TYPES).get("minecraft:stone"));
                        element.setModelPrototype(new Identifier("minecraft:cube_all"));
                        element.setItemModelPrototype(ModelManager.INHERIT);
                    })
                    .editorFactory(BlockEditor::new)
                    .build();
    public static final ElementType<MEItem> ITEM =
            ElementType.builder("item", MEItem.class)
                    .createdHandler((project, element, file, identifier, name) -> {
                        final IndexManager indexManager = IndexManager.getInstance(project);
                        element.setIdentifier(identifier);
                        element.setDisplayName(name);
                        element.setEquipmentSlot(EquipmentSlot.MAINHAND);
                        element.setItemGroup(Iterables.getFirst(indexManager.getIndex(Indexes.ITEM_GROUPS).values(), null));
                        element.setModelPrototype(new Identifier("minecraft:generated"));
                        element.setEquipSound(indexManager.getIndex(Indexes.SOUND_EVENTS).get("minecraft:item.armor.equip_generic"));
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
