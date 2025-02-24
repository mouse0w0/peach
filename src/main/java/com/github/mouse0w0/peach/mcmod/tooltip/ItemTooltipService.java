package com.github.mouse0w0.peach.mcmod.tooltip;

import com.github.mouse0w0.peach.project.Project;

public interface ItemTooltipService {
    static ItemTooltipService getInstance(Project project) {
        return project.getService(ItemTooltipService.class);
    }

    void install(ItemTooltipProvider provider);

    void uninstall(ItemTooltipProvider provider);
}
