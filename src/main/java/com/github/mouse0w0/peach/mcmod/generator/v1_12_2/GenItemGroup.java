package com.github.mouse0w0.peach.mcmod.generator.v1_12_2;

import com.github.mouse0w0.peach.mcmod.element.impl.ItemGroupElement;
import com.github.mouse0w0.peach.mcmod.generator.Context;
import com.github.mouse0w0.peach.mcmod.generator.task.Task;
import com.github.mouse0w0.peach.mcmod.generator.util.JavaUtils;
import com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode.ItemGroupClassGenerator;
import com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode.ItemGroupLoaderClassGenerator;
import com.github.mouse0w0.peach.util.StringUtils;

public class GenItemGroup implements Task {
    @Override
    public void run(Context context) throws Exception {
        ItemGroupLoaderClassGenerator loader = new ItemGroupLoaderClassGenerator(context.getInternalName("itemGroup/ItemGroupLoader"));

        for (ItemGroupElement itemGroup : context.getElements(ItemGroupElement.class)) {
            String className = context.getInternalName("itemGroup/" + JavaUtils.lowerUnderscoreToUpperCamel(itemGroup.getIdentifier()) + "ItemGroup");

            ItemGroupClassGenerator cg = new ItemGroupClassGenerator(className);
            cg.visitIdentifier(itemGroup.getIdentifier());
            cg.visitTranslationKey(context.getTranslationKey("itemGroup", itemGroup.getIdentifier()));
            cg.visitIcon(context, itemGroup.getIcon());
            if (itemGroup.isHasSearchBar()) {
                cg.visitHasSearchBar();
            }
            if (StringUtils.isNotEmpty(itemGroup.getBackground())) {
                cg.visitBackground(context.getResourceKey("textures", itemGroup.getBackground()));
            } else {
                if (itemGroup.isHasSearchBar()) {
                    cg.visitBackgroundWithSearchBar();
                }
            }
            context.getClassesFiler().write(cg.getThisName() + ".class", cg.toByteArray());

            loader.visitItemGroup(className);

            String background = itemGroup.getBackground();
            if (StringUtils.isNotEmpty(background)) {
                context.getAssetsFiler().copy(context.getTexturesFolder().resolve(background), "textures/" + background);
            }
        }
        context.getClassesFiler().write(loader.getThisName() + ".class", loader.toByteArray());
    }
}
