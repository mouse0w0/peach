package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2;

import com.github.mouse0w0.peach.mcmod.compiler.Context;
import com.github.mouse0w0.peach.mcmod.compiler.task.Task;
import com.github.mouse0w0.peach.mcmod.compiler.util.JavaUtils;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.bytecode.ItemGroupClassGenerator;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.bytecode.ItemGroupLoaderClassGenerator;
import com.github.mouse0w0.peach.mcmod.element.ElementTypes;
import com.github.mouse0w0.peach.mcmod.element.impl.MEItemGroup;
import com.github.mouse0w0.peach.util.StringUtils;

public class GenItemGroup implements Task {
    @Override
    public void run(Context context) throws Exception {
        ItemGroupLoaderClassGenerator loader = new ItemGroupLoaderClassGenerator(context.getInternalName("itemGroup/ItemGroupLoader"));

        for (MEItemGroup itemGroup : context.getElements(ElementTypes.ITEM_GROUP)) {
            String className = context.getInternalName("itemGroup/" + JavaUtils.lowerUnderscoreToUpperCamel(itemGroup.getIdentifier()) + "ItemGroup");

            ItemGroupClassGenerator cg = new ItemGroupClassGenerator(className);
            cg.visitIdentifier(itemGroup.getIdentifier());
            cg.visitTranslationKey(context.getTranslationKey("itemGroup", itemGroup.getIdentifier()));
            cg.visitIcon(itemGroup.getIcon());
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
                context.getAssetsFiler().copy(context.getProjectStructure().getTextures().resolve(background), "textures/" + background);
            }
        }
        context.getClassesFiler().write(loader.getThisName() + ".class", loader.toByteArray());
    }
}
