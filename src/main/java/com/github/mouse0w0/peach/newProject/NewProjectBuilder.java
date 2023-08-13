package com.github.mouse0w0.peach.newProject;

import com.github.mouse0w0.peach.project.Project;
import javafx.scene.Node;

public interface NewProjectBuilder {
    Node getNode();

    void initialize(NewProjectContext context);

    void commitData(NewProjectContext context);

    boolean validate();

    void setupProject(Project project, NewProjectContext context);
}
