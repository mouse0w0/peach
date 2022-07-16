package com.github.mouse0w0.peach.mcmod.generator.v1_12_2;

import com.github.mouse0w0.peach.mcmod.element.impl.MEBlock;
import com.github.mouse0w0.peach.mcmod.element.impl.MEItem;
import com.github.mouse0w0.peach.mcmod.generator.Context;
import com.github.mouse0w0.peach.mcmod.generator.task.Task;
import com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode.ItemGroupsClassGenerator;

public class GenItemGroupConstants implements Task {
    @Override
    public void run(Context context) throws Exception {
        ItemGroupsClassGenerator groups = new ItemGroupsClassGenerator(context.getInternalName("init/ItemGroups"));

        for (MEBlock block : context.getElements(MEBlock.class)) {
            groups.visitItemGroup(block.getItemGroup().getId());
        }

        for (MEItem item : context.getElements(MEItem.class)) {
            groups.visitItemGroup(item.getItemGroup().getId());
        }

        context.getClassesFiler().write(groups.getThisName() + ".class", groups.toByteArray());
    }
}
