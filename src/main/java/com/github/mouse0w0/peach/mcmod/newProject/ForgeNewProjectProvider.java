package com.github.mouse0w0.peach.mcmod.newProject;

import com.github.mouse0w0.peach.icon.AppIcon;
import com.github.mouse0w0.peach.icon.Icon;
import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.newProject.NewProjectBuilder;
import com.github.mouse0w0.peach.newProject.NewProjectProvider;

public final class ForgeNewProjectProvider implements NewProjectProvider {
    @Override
    public String getName() {
        return AppL10n.localize("newProject.forge.name");
    }

    @Override
    public String getDescription() {
        return AppL10n.localize("newProject.forge.description");
    }

    @Override
    public Icon getIcon() {
        return AppIcon.File.Forge;
    }

    @Override
    public NewProjectBuilder createBuilder() {
        return new ForgeNewProjectBuilder();
    }
}
