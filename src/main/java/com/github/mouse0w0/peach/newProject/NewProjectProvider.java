package com.github.mouse0w0.peach.newProject;

import com.github.mouse0w0.peach.extension.ExtensionPointName;
import com.github.mouse0w0.peach.icon.Icon;

public interface NewProjectProvider {
    ExtensionPointName<NewProjectProvider> EXTENSION_POINT = ExtensionPointName.of("peach.newProjectProvider");

    String getName();

    String getDescription();

    Icon getIcon();

    NewProjectBuilder createBuilder();
}
