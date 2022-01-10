package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.generator;

import com.github.mouse0w0.peach.mcmod.compiler.Context;
import com.github.mouse0w0.peach.mcmod.compiler.util.JavaUtils;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.bytecode.ItemGroupClassGenerator;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.bytecode.ItemGroupLoaderClassGenerator;
import com.github.mouse0w0.peach.mcmod.element.impl.MEItemGroup;
import com.google.common.base.Strings;

import java.util.Collection;

public class ItemGroupGenerator extends Generator<MEItemGroup> {

    private ItemGroupLoaderClassGenerator loaderClassGenerator;

    @Override
    protected void before(Context context, Collection<MEItemGroup> elements) throws Exception {
        loaderClassGenerator = new ItemGroupLoaderClassGenerator(context.getInternalName("itemGroup/ItemGroupLoader"));
    }

    @Override
    protected void after(Context context, Collection<MEItemGroup> elements) throws Exception {
        context.getClassesFiler().write(loaderClassGenerator.getThisName() + ".class", loaderClassGenerator.toByteArray());
    }

    @Override
    protected void generate(Context context, MEItemGroup itemGroup) throws Exception {
        String className = context.getInternalName("itemGroup/" + JavaUtils.lowerUnderscoreToUpperCamel(itemGroup.getIdentifier()) + "ItemGroup");

        ItemGroupClassGenerator cg = new ItemGroupClassGenerator(className);
        cg.visitIdentifier(itemGroup.getIdentifier());
        cg.visitTranslationKey(context.getTranslationKey("itemGroup", itemGroup.getIdentifier()));
        cg.visitIcon(itemGroup.getIcon());
        if (itemGroup.isHasSearchBar()) {
            cg.visitHasSearchBar();
        }
        if (!Strings.isNullOrEmpty(itemGroup.getBackground())) {
            cg.visitBackground(context.getResourceKey("textures", itemGroup.getBackground()));
        } else {
            if (itemGroup.isHasSearchBar()) {
                cg.visitBackgroundWithSearchBar();
            }
        }
        context.getClassesFiler().write(cg.getThisName() + ".class", cg.toByteArray());

        loaderClassGenerator.visitItemGroup(className);

        String background = itemGroup.getBackground();
        if (!Strings.isNullOrEmpty(background)) {
            context.getAssetsFiler().copy(context.getProjectStructure().getTextures().resolve(background), "textures/" + background);
        }
    }
}
