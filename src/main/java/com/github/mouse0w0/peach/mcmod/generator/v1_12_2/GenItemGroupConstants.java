package com.github.mouse0w0.peach.mcmod.generator.v1_12_2;

import com.github.mouse0w0.peach.mcmod.element.impl.BlockElement;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemElement;
import com.github.mouse0w0.peach.mcmod.generator.Context;
import com.github.mouse0w0.peach.mcmod.generator.task.Task;
import com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode.ItemGroupsClassGenerator;

public class GenItemGroupConstants implements Task {
    @Override
    public void run(Context context) throws Exception {
        ItemGroupsClassGenerator groups = new ItemGroupsClassGenerator(context.getInternalName("init/ItemGroups"));

        for (BlockElement block : context.getElements(BlockElement.class)) {
            groups.visitItemGroup(block.getItemGroup().getId());
        }

        for (ItemElement item : context.getElements(ItemElement.class)) {
            groups.visitItemGroup(item.getItemGroup().getId());
        }

        context.getClassesFiler().write(groups.getThisName() + ".class", groups.toByteArray());
    }
}
