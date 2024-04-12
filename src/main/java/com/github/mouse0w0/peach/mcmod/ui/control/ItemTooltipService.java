package com.github.mouse0w0.peach.mcmod.ui.control;

import com.github.mouse0w0.peach.project.Project;

public interface ItemTooltipService {
    static ItemTooltipService getInstance(Project project) {
        return project.getService(ItemTooltipService.class);
    }

    void install(ItemView itemView);

    void uninstall(ItemView itemView);
}
